import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAuthor } from 'app/entities/author/author.model';
import { AuthorService } from 'app/entities/author/service/author.service';
import { IBook } from 'app/entities/book/book.model';
import { BookService } from 'app/entities/book/service/book.service';
import { BookAuthorService } from '../service/book-author.service';
import { IBookAuthor } from '../book-author.model';
import { BookAuthorFormGroup, BookAuthorFormService } from './book-author-form.service';

@Component({
  selector: 'jhi-book-author-update',
  templateUrl: './book-author-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BookAuthorUpdateComponent implements OnInit {
  isSaving = false;
  bookAuthor: IBookAuthor | null = null;

  authorsSharedCollection: IAuthor[] = [];
  booksSharedCollection: IBook[] = [];

  protected bookAuthorService = inject(BookAuthorService);
  protected bookAuthorFormService = inject(BookAuthorFormService);
  protected authorService = inject(AuthorService);
  protected bookService = inject(BookService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BookAuthorFormGroup = this.bookAuthorFormService.createBookAuthorFormGroup();

  compareAuthor = (o1: IAuthor | null, o2: IAuthor | null): boolean => this.authorService.compareAuthor(o1, o2);

  compareBook = (o1: IBook | null, o2: IBook | null): boolean => this.bookService.compareBook(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bookAuthor }) => {
      this.bookAuthor = bookAuthor;
      if (bookAuthor) {
        this.updateForm(bookAuthor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bookAuthor = this.bookAuthorFormService.getBookAuthor(this.editForm);
    if (bookAuthor.id !== null) {
      this.subscribeToSaveResponse(this.bookAuthorService.update(bookAuthor));
    } else {
      this.subscribeToSaveResponse(this.bookAuthorService.create(bookAuthor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBookAuthor>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(bookAuthor: IBookAuthor): void {
    this.bookAuthor = bookAuthor;
    this.bookAuthorFormService.resetForm(this.editForm, bookAuthor);

    this.authorsSharedCollection = this.authorService.addAuthorToCollectionIfMissing<IAuthor>(
      this.authorsSharedCollection,
      bookAuthor.author,
    );
    this.booksSharedCollection = this.bookService.addBookToCollectionIfMissing<IBook>(this.booksSharedCollection, bookAuthor.book);
  }

  protected loadRelationshipsOptions(): void {
    this.authorService
      .query()
      .pipe(map((res: HttpResponse<IAuthor[]>) => res.body ?? []))
      .pipe(map((authors: IAuthor[]) => this.authorService.addAuthorToCollectionIfMissing<IAuthor>(authors, this.bookAuthor?.author)))
      .subscribe((authors: IAuthor[]) => (this.authorsSharedCollection = authors));

    this.bookService
      .query()
      .pipe(map((res: HttpResponse<IBook[]>) => res.body ?? []))
      .pipe(map((books: IBook[]) => this.bookService.addBookToCollectionIfMissing<IBook>(books, this.bookAuthor?.book)))
      .subscribe((books: IBook[]) => (this.booksSharedCollection = books));
  }
}
