import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IBorrowedBook } from '../borrowed-book.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../borrowed-book.test-samples';

import { BorrowedBookService, RestBorrowedBook } from './borrowed-book.service';

const requireRestSample: RestBorrowedBook = {
  ...sampleWithRequiredData,
  borrowDate: sampleWithRequiredData.borrowDate?.toJSON(),
};

describe('BorrowedBook Service', () => {
  let service: BorrowedBookService;
  let httpMock: HttpTestingController;
  let expectedResult: IBorrowedBook | IBorrowedBook[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BorrowedBookService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a BorrowedBook', () => {
      const borrowedBook = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(borrowedBook).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BorrowedBook', () => {
      const borrowedBook = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(borrowedBook).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BorrowedBook', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BorrowedBook', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BorrowedBook', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBorrowedBookToCollectionIfMissing', () => {
      it('should add a BorrowedBook to an empty array', () => {
        const borrowedBook: IBorrowedBook = sampleWithRequiredData;
        expectedResult = service.addBorrowedBookToCollectionIfMissing([], borrowedBook);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(borrowedBook);
      });

      it('should not add a BorrowedBook to an array that contains it', () => {
        const borrowedBook: IBorrowedBook = sampleWithRequiredData;
        const borrowedBookCollection: IBorrowedBook[] = [
          {
            ...borrowedBook,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBorrowedBookToCollectionIfMissing(borrowedBookCollection, borrowedBook);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BorrowedBook to an array that doesn't contain it", () => {
        const borrowedBook: IBorrowedBook = sampleWithRequiredData;
        const borrowedBookCollection: IBorrowedBook[] = [sampleWithPartialData];
        expectedResult = service.addBorrowedBookToCollectionIfMissing(borrowedBookCollection, borrowedBook);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(borrowedBook);
      });

      it('should add only unique BorrowedBook to an array', () => {
        const borrowedBookArray: IBorrowedBook[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const borrowedBookCollection: IBorrowedBook[] = [sampleWithRequiredData];
        expectedResult = service.addBorrowedBookToCollectionIfMissing(borrowedBookCollection, ...borrowedBookArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const borrowedBook: IBorrowedBook = sampleWithRequiredData;
        const borrowedBook2: IBorrowedBook = sampleWithPartialData;
        expectedResult = service.addBorrowedBookToCollectionIfMissing([], borrowedBook, borrowedBook2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(borrowedBook);
        expect(expectedResult).toContain(borrowedBook2);
      });

      it('should accept null and undefined values', () => {
        const borrowedBook: IBorrowedBook = sampleWithRequiredData;
        expectedResult = service.addBorrowedBookToCollectionIfMissing([], null, borrowedBook, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(borrowedBook);
      });

      it('should return initial array if no BorrowedBook is added', () => {
        const borrowedBookCollection: IBorrowedBook[] = [sampleWithRequiredData];
        expectedResult = service.addBorrowedBookToCollectionIfMissing(borrowedBookCollection, undefined, null);
        expect(expectedResult).toEqual(borrowedBookCollection);
      });
    });

    describe('compareBorrowedBook', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBorrowedBook(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 10580 };
        const entity2 = null;

        const compareResult1 = service.compareBorrowedBook(entity1, entity2);
        const compareResult2 = service.compareBorrowedBook(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 10580 };
        const entity2 = { id: 20744 };

        const compareResult1 = service.compareBorrowedBook(entity1, entity2);
        const compareResult2 = service.compareBorrowedBook(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 10580 };
        const entity2 = { id: 10580 };

        const compareResult1 = service.compareBorrowedBook(entity1, entity2);
        const compareResult2 = service.compareBorrowedBook(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
