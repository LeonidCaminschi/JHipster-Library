import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPublisher } from '../publisher.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../publisher.test-samples';

import { PublisherService } from './publisher.service';

const requireRestSample: IPublisher = {
  ...sampleWithRequiredData,
};

describe('Publisher Service', () => {
  let service: PublisherService;
  let httpMock: HttpTestingController;
  let expectedResult: IPublisher | IPublisher[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PublisherService);
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

    it('should create a Publisher', () => {
      const publisher = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(publisher).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Publisher', () => {
      const publisher = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(publisher).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Publisher', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Publisher', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Publisher', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPublisherToCollectionIfMissing', () => {
      it('should add a Publisher to an empty array', () => {
        const publisher: IPublisher = sampleWithRequiredData;
        expectedResult = service.addPublisherToCollectionIfMissing([], publisher);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(publisher);
      });

      it('should not add a Publisher to an array that contains it', () => {
        const publisher: IPublisher = sampleWithRequiredData;
        const publisherCollection: IPublisher[] = [
          {
            ...publisher,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPublisherToCollectionIfMissing(publisherCollection, publisher);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Publisher to an array that doesn't contain it", () => {
        const publisher: IPublisher = sampleWithRequiredData;
        const publisherCollection: IPublisher[] = [sampleWithPartialData];
        expectedResult = service.addPublisherToCollectionIfMissing(publisherCollection, publisher);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(publisher);
      });

      it('should add only unique Publisher to an array', () => {
        const publisherArray: IPublisher[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const publisherCollection: IPublisher[] = [sampleWithRequiredData];
        expectedResult = service.addPublisherToCollectionIfMissing(publisherCollection, ...publisherArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const publisher: IPublisher = sampleWithRequiredData;
        const publisher2: IPublisher = sampleWithPartialData;
        expectedResult = service.addPublisherToCollectionIfMissing([], publisher, publisher2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(publisher);
        expect(expectedResult).toContain(publisher2);
      });

      it('should accept null and undefined values', () => {
        const publisher: IPublisher = sampleWithRequiredData;
        expectedResult = service.addPublisherToCollectionIfMissing([], null, publisher, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(publisher);
      });

      it('should return initial array if no Publisher is added', () => {
        const publisherCollection: IPublisher[] = [sampleWithRequiredData];
        expectedResult = service.addPublisherToCollectionIfMissing(publisherCollection, undefined, null);
        expect(expectedResult).toEqual(publisherCollection);
      });
    });

    describe('comparePublisher', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePublisher(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 14789 };
        const entity2 = null;

        const compareResult1 = service.comparePublisher(entity1, entity2);
        const compareResult2 = service.comparePublisher(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 14789 };
        const entity2 = { id: 9111 };

        const compareResult1 = service.comparePublisher(entity1, entity2);
        const compareResult2 = service.comparePublisher(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 14789 };
        const entity2 = { id: 14789 };

        const compareResult1 = service.comparePublisher(entity1, entity2);
        const compareResult2 = service.comparePublisher(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
