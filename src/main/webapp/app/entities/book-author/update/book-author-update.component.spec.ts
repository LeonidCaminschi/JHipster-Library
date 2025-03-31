import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAuthor } from 'app/entities/author/author.model';
import { AuthorService } from 'app/entities/author/service/author.service';
import { IBook } from 'app/entities/book/book.model';
import { BookService } from 'app/entities/book/service/book.service';
import { IBookAuthor } from '../book-author.model';
import { BookAuthorService } from '../service/book-author.service';
import { BookAuthorFormService } from './book-author-form.service';

import { BookAuthorUpdateComponent } from './book-author-update.component';

describe('BookAuthor Management Update Component', () => {
  let comp: BookAuthorUpdateComponent;
  let fixture: ComponentFixture<BookAuthorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bookAuthorFormService: BookAuthorFormService;
  let bookAuthorService: BookAuthorService;
  let authorService: AuthorService;
  let bookService: BookService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BookAuthorUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BookAuthorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BookAuthorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bookAuthorFormService = TestBed.inject(BookAuthorFormService);
    bookAuthorService = TestBed.inject(BookAuthorService);
    authorService = TestBed.inject(AuthorService);
    bookService = TestBed.inject(BookService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Author query and add missing value', () => {
      const bookAuthor: IBookAuthor = { id: 31165 };
      const author: IAuthor = { id: 32542 };
      bookAuthor.author = author;

      const authorCollection: IAuthor[] = [{ id: 32542 }];
      jest.spyOn(authorService, 'query').mockReturnValue(of(new HttpResponse({ body: authorCollection })));
      const additionalAuthors = [author];
      const expectedCollection: IAuthor[] = [...additionalAuthors, ...authorCollection];
      jest.spyOn(authorService, 'addAuthorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bookAuthor });
      comp.ngOnInit();

      expect(authorService.query).toHaveBeenCalled();
      expect(authorService.addAuthorToCollectionIfMissing).toHaveBeenCalledWith(
        authorCollection,
        ...additionalAuthors.map(expect.objectContaining),
      );
      expect(comp.authorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Book query and add missing value', () => {
      const bookAuthor: IBookAuthor = { id: 31165 };
      const book: IBook = { id: 32624 };
      bookAuthor.book = book;

      const bookCollection: IBook[] = [{ id: 32624 }];
      jest.spyOn(bookService, 'query').mockReturnValue(of(new HttpResponse({ body: bookCollection })));
      const additionalBooks = [book];
      const expectedCollection: IBook[] = [...additionalBooks, ...bookCollection];
      jest.spyOn(bookService, 'addBookToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bookAuthor });
      comp.ngOnInit();

      expect(bookService.query).toHaveBeenCalled();
      expect(bookService.addBookToCollectionIfMissing).toHaveBeenCalledWith(
        bookCollection,
        ...additionalBooks.map(expect.objectContaining),
      );
      expect(comp.booksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bookAuthor: IBookAuthor = { id: 31165 };
      const author: IAuthor = { id: 32542 };
      bookAuthor.author = author;
      const book: IBook = { id: 32624 };
      bookAuthor.book = book;

      activatedRoute.data = of({ bookAuthor });
      comp.ngOnInit();

      expect(comp.authorsSharedCollection).toContainEqual(author);
      expect(comp.booksSharedCollection).toContainEqual(book);
      expect(comp.bookAuthor).toEqual(bookAuthor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookAuthor>>();
      const bookAuthor = { id: 16660 };
      jest.spyOn(bookAuthorFormService, 'getBookAuthor').mockReturnValue(bookAuthor);
      jest.spyOn(bookAuthorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookAuthor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bookAuthor }));
      saveSubject.complete();

      // THEN
      expect(bookAuthorFormService.getBookAuthor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bookAuthorService.update).toHaveBeenCalledWith(expect.objectContaining(bookAuthor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookAuthor>>();
      const bookAuthor = { id: 16660 };
      jest.spyOn(bookAuthorFormService, 'getBookAuthor').mockReturnValue({ id: null });
      jest.spyOn(bookAuthorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookAuthor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bookAuthor }));
      saveSubject.complete();

      // THEN
      expect(bookAuthorFormService.getBookAuthor).toHaveBeenCalled();
      expect(bookAuthorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookAuthor>>();
      const bookAuthor = { id: 16660 };
      jest.spyOn(bookAuthorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookAuthor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bookAuthorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAuthor', () => {
      it('Should forward to authorService', () => {
        const entity = { id: 32542 };
        const entity2 = { id: 11676 };
        jest.spyOn(authorService, 'compareAuthor');
        comp.compareAuthor(entity, entity2);
        expect(authorService.compareAuthor).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBook', () => {
      it('Should forward to bookService', () => {
        const entity = { id: 32624 };
        const entity2 = { id: 17120 };
        jest.spyOn(bookService, 'compareBook');
        comp.compareBook(entity, entity2);
        expect(bookService.compareBook).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
