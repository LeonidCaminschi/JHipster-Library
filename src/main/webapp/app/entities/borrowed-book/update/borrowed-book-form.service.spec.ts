import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../borrowed-book.test-samples';

import { BorrowedBookFormService } from './borrowed-book-form.service';

describe('BorrowedBook Form Service', () => {
  let service: BorrowedBookFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BorrowedBookFormService);
  });

  describe('Service methods', () => {
    describe('createBorrowedBookFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBorrowedBookFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            borrowDate: expect.any(Object),
            book: expect.any(Object),
            client: expect.any(Object),
          }),
        );
      });

      it('passing IBorrowedBook should create a new form with FormGroup', () => {
        const formGroup = service.createBorrowedBookFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            borrowDate: expect.any(Object),
            book: expect.any(Object),
            client: expect.any(Object),
          }),
        );
      });
    });

    describe('getBorrowedBook', () => {
      it('should return NewBorrowedBook for default BorrowedBook initial value', () => {
        const formGroup = service.createBorrowedBookFormGroup(sampleWithNewData);

        const borrowedBook = service.getBorrowedBook(formGroup) as any;

        expect(borrowedBook).toMatchObject(sampleWithNewData);
      });

      it('should return NewBorrowedBook for empty BorrowedBook initial value', () => {
        const formGroup = service.createBorrowedBookFormGroup();

        const borrowedBook = service.getBorrowedBook(formGroup) as any;

        expect(borrowedBook).toMatchObject({});
      });

      it('should return IBorrowedBook', () => {
        const formGroup = service.createBorrowedBookFormGroup(sampleWithRequiredData);

        const borrowedBook = service.getBorrowedBook(formGroup) as any;

        expect(borrowedBook).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBorrowedBook should not enable id FormControl', () => {
        const formGroup = service.createBorrowedBookFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBorrowedBook should disable id FormControl', () => {
        const formGroup = service.createBorrowedBookFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
