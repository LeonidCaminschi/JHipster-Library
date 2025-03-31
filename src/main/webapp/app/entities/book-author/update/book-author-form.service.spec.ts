import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../book-author.test-samples';

import { BookAuthorFormService } from './book-author-form.service';

describe('BookAuthor Form Service', () => {
  let service: BookAuthorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookAuthorFormService);
  });

  describe('Service methods', () => {
    describe('createBookAuthorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBookAuthorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            author: expect.any(Object),
            book: expect.any(Object),
          }),
        );
      });

      it('passing IBookAuthor should create a new form with FormGroup', () => {
        const formGroup = service.createBookAuthorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            author: expect.any(Object),
            book: expect.any(Object),
          }),
        );
      });
    });

    describe('getBookAuthor', () => {
      it('should return NewBookAuthor for default BookAuthor initial value', () => {
        const formGroup = service.createBookAuthorFormGroup(sampleWithNewData);

        const bookAuthor = service.getBookAuthor(formGroup) as any;

        expect(bookAuthor).toMatchObject(sampleWithNewData);
      });

      it('should return NewBookAuthor for empty BookAuthor initial value', () => {
        const formGroup = service.createBookAuthorFormGroup();

        const bookAuthor = service.getBookAuthor(formGroup) as any;

        expect(bookAuthor).toMatchObject({});
      });

      it('should return IBookAuthor', () => {
        const formGroup = service.createBookAuthorFormGroup(sampleWithRequiredData);

        const bookAuthor = service.getBookAuthor(formGroup) as any;

        expect(bookAuthor).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBookAuthor should not enable id FormControl', () => {
        const formGroup = service.createBookAuthorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBookAuthor should disable id FormControl', () => {
        const formGroup = service.createBookAuthorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
