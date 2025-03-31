import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBookAuthor, NewBookAuthor } from '../book-author.model';

export type PartialUpdateBookAuthor = Partial<IBookAuthor> & Pick<IBookAuthor, 'id'>;

export type EntityResponseType = HttpResponse<IBookAuthor>;
export type EntityArrayResponseType = HttpResponse<IBookAuthor[]>;

@Injectable({ providedIn: 'root' })
export class BookAuthorService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/book-authors');

  create(bookAuthor: NewBookAuthor): Observable<EntityResponseType> {
    return this.http.post<IBookAuthor>(this.resourceUrl, bookAuthor, { observe: 'response' });
  }

  update(bookAuthor: IBookAuthor): Observable<EntityResponseType> {
    return this.http.put<IBookAuthor>(`${this.resourceUrl}/${this.getBookAuthorIdentifier(bookAuthor)}`, bookAuthor, {
      observe: 'response',
    });
  }

  partialUpdate(bookAuthor: PartialUpdateBookAuthor): Observable<EntityResponseType> {
    return this.http.patch<IBookAuthor>(`${this.resourceUrl}/${this.getBookAuthorIdentifier(bookAuthor)}`, bookAuthor, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBookAuthor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBookAuthor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBookAuthorIdentifier(bookAuthor: Pick<IBookAuthor, 'id'>): number {
    return bookAuthor.id;
  }

  compareBookAuthor(o1: Pick<IBookAuthor, 'id'> | null, o2: Pick<IBookAuthor, 'id'> | null): boolean {
    return o1 && o2 ? this.getBookAuthorIdentifier(o1) === this.getBookAuthorIdentifier(o2) : o1 === o2;
  }

  addBookAuthorToCollectionIfMissing<Type extends Pick<IBookAuthor, 'id'>>(
    bookAuthorCollection: Type[],
    ...bookAuthorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bookAuthors: Type[] = bookAuthorsToCheck.filter(isPresent);
    if (bookAuthors.length > 0) {
      const bookAuthorCollectionIdentifiers = bookAuthorCollection.map(bookAuthorItem => this.getBookAuthorIdentifier(bookAuthorItem));
      const bookAuthorsToAdd = bookAuthors.filter(bookAuthorItem => {
        const bookAuthorIdentifier = this.getBookAuthorIdentifier(bookAuthorItem);
        if (bookAuthorCollectionIdentifiers.includes(bookAuthorIdentifier)) {
          return false;
        }
        bookAuthorCollectionIdentifiers.push(bookAuthorIdentifier);
        return true;
      });
      return [...bookAuthorsToAdd, ...bookAuthorCollection];
    }
    return bookAuthorCollection;
  }
}
