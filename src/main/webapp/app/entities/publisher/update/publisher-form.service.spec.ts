import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../publisher.test-samples';

import { PublisherFormService } from './publisher-form.service';

describe('Publisher Form Service', () => {
  let service: PublisherFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PublisherFormService);
  });

  describe('Service methods', () => {
    describe('createPublisherFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPublisherFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing IPublisher should create a new form with FormGroup', () => {
        const formGroup = service.createPublisherFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getPublisher', () => {
      it('should return NewPublisher for default Publisher initial value', () => {
        const formGroup = service.createPublisherFormGroup(sampleWithNewData);

        const publisher = service.getPublisher(formGroup) as any;

        expect(publisher).toMatchObject(sampleWithNewData);
      });

      it('should return NewPublisher for empty Publisher initial value', () => {
        const formGroup = service.createPublisherFormGroup();

        const publisher = service.getPublisher(formGroup) as any;

        expect(publisher).toMatchObject({});
      });

      it('should return IPublisher', () => {
        const formGroup = service.createPublisherFormGroup(sampleWithRequiredData);

        const publisher = service.getPublisher(formGroup) as any;

        expect(publisher).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPublisher should not enable id FormControl', () => {
        const formGroup = service.createPublisherFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPublisher should disable id FormControl', () => {
        const formGroup = service.createPublisherFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
