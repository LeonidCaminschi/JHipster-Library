import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBorrowedBook, NewBorrowedBook } from '../borrowed-book.model';

export type PartialUpdateBorrowedBook = Partial<IBorrowedBook> & Pick<IBorrowedBook, 'id'>;

type RestOf<T extends IBorrowedBook | NewBorrowedBook> = Omit<T, 'borrowDate'> & {
  borrowDate?: string | null;
};

export type RestBorrowedBook = RestOf<IBorrowedBook>;

export type NewRestBorrowedBook = RestOf<NewBorrowedBook>;

export type PartialUpdateRestBorrowedBook = RestOf<PartialUpdateBorrowedBook>;

export type EntityResponseType = HttpResponse<IBorrowedBook>;
export type EntityArrayResponseType = HttpResponse<IBorrowedBook[]>;

@Injectable({ providedIn: 'root' })
export class BorrowedBookService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/borrowed-books');

  create(borrowedBook: NewBorrowedBook): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(borrowedBook);
    return this.http
      .post<RestBorrowedBook>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(borrowedBook: IBorrowedBook): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(borrowedBook);
    return this.http
      .put<RestBorrowedBook>(`${this.resourceUrl}/${this.getBorrowedBookIdentifier(borrowedBook)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(borrowedBook: PartialUpdateBorrowedBook): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(borrowedBook);
    return this.http
      .patch<RestBorrowedBook>(`${this.resourceUrl}/${this.getBorrowedBookIdentifier(borrowedBook)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBorrowedBook>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBorrowedBook[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBorrowedBookIdentifier(borrowedBook: Pick<IBorrowedBook, 'id'>): number {
    return borrowedBook.id;
  }

  compareBorrowedBook(o1: Pick<IBorrowedBook, 'id'> | null, o2: Pick<IBorrowedBook, 'id'> | null): boolean {
    return o1 && o2 ? this.getBorrowedBookIdentifier(o1) === this.getBorrowedBookIdentifier(o2) : o1 === o2;
  }

  addBorrowedBookToCollectionIfMissing<Type extends Pick<IBorrowedBook, 'id'>>(
    borrowedBookCollection: Type[],
    ...borrowedBooksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const borrowedBooks: Type[] = borrowedBooksToCheck.filter(isPresent);
    if (borrowedBooks.length > 0) {
      const borrowedBookCollectionIdentifiers = borrowedBookCollection.map(borrowedBookItem =>
        this.getBorrowedBookIdentifier(borrowedBookItem),
      );
      const borrowedBooksToAdd = borrowedBooks.filter(borrowedBookItem => {
        const borrowedBookIdentifier = this.getBorrowedBookIdentifier(borrowedBookItem);
        if (borrowedBookCollectionIdentifiers.includes(borrowedBookIdentifier)) {
          return false;
        }
        borrowedBookCollectionIdentifiers.push(borrowedBookIdentifier);
        return true;
      });
      return [...borrowedBooksToAdd, ...borrowedBookCollection];
    }
    return borrowedBookCollection;
  }

  protected convertDateFromClient<T extends IBorrowedBook | NewBorrowedBook | PartialUpdateBorrowedBook>(borrowedBook: T): RestOf<T> {
    return {
      ...borrowedBook,
      borrowDate: borrowedBook.borrowDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBorrowedBook: RestBorrowedBook): IBorrowedBook {
    return {
      ...restBorrowedBook,
      borrowDate: restBorrowedBook.borrowDate ? dayjs(restBorrowedBook.borrowDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBorrowedBook>): HttpResponse<IBorrowedBook> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBorrowedBook[]>): HttpResponse<IBorrowedBook[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
