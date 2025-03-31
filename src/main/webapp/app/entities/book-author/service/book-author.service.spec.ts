import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IBookAuthor } from '../book-author.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../book-author.test-samples';

import { BookAuthorService } from './book-author.service';

const requireRestSample: IBookAuthor = {
  ...sampleWithRequiredData,
};

describe('BookAuthor Service', () => {
  let service: BookAuthorService;
  let httpMock: HttpTestingController;
  let expectedResult: IBookAuthor | IBookAuthor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BookAuthorService);
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

    it('should create a BookAuthor', () => {
      const bookAuthor = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bookAuthor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BookAuthor', () => {
      const bookAuthor = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bookAuthor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BookAuthor', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BookAuthor', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BookAuthor', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBookAuthorToCollectionIfMissing', () => {
      it('should add a BookAuthor to an empty array', () => {
        const bookAuthor: IBookAuthor = sampleWithRequiredData;
        expectedResult = service.addBookAuthorToCollectionIfMissing([], bookAuthor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bookAuthor);
      });

      it('should not add a BookAuthor to an array that contains it', () => {
        const bookAuthor: IBookAuthor = sampleWithRequiredData;
        const bookAuthorCollection: IBookAuthor[] = [
          {
            ...bookAuthor,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBookAuthorToCollectionIfMissing(bookAuthorCollection, bookAuthor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BookAuthor to an array that doesn't contain it", () => {
        const bookAuthor: IBookAuthor = sampleWithRequiredData;
        const bookAuthorCollection: IBookAuthor[] = [sampleWithPartialData];
        expectedResult = service.addBookAuthorToCollectionIfMissing(bookAuthorCollection, bookAuthor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bookAuthor);
      });

      it('should add only unique BookAuthor to an array', () => {
        const bookAuthorArray: IBookAuthor[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bookAuthorCollection: IBookAuthor[] = [sampleWithRequiredData];
        expectedResult = service.addBookAuthorToCollectionIfMissing(bookAuthorCollection, ...bookAuthorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bookAuthor: IBookAuthor = sampleWithRequiredData;
        const bookAuthor2: IBookAuthor = sampleWithPartialData;
        expectedResult = service.addBookAuthorToCollectionIfMissing([], bookAuthor, bookAuthor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bookAuthor);
        expect(expectedResult).toContain(bookAuthor2);
      });

      it('should accept null and undefined values', () => {
        const bookAuthor: IBookAuthor = sampleWithRequiredData;
        expectedResult = service.addBookAuthorToCollectionIfMissing([], null, bookAuthor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bookAuthor);
      });

      it('should return initial array if no BookAuthor is added', () => {
        const bookAuthorCollection: IBookAuthor[] = [sampleWithRequiredData];
        expectedResult = service.addBookAuthorToCollectionIfMissing(bookAuthorCollection, undefined, null);
        expect(expectedResult).toEqual(bookAuthorCollection);
      });
    });

    describe('compareBookAuthor', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBookAuthor(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 16660 };
        const entity2 = null;

        const compareResult1 = service.compareBookAuthor(entity1, entity2);
        const compareResult2 = service.compareBookAuthor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 16660 };
        const entity2 = { id: 31165 };

        const compareResult1 = service.compareBookAuthor(entity1, entity2);
        const compareResult2 = service.compareBookAuthor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 16660 };
        const entity2 = { id: 16660 };

        const compareResult1 = service.compareBookAuthor(entity1, entity2);
        const compareResult2 = service.compareBookAuthor(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
