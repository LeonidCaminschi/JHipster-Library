import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PublisherService } from '../service/publisher.service';
import { IPublisher } from '../publisher.model';
import { PublisherFormService } from './publisher-form.service';

import { PublisherUpdateComponent } from './publisher-update.component';

describe('Publisher Management Update Component', () => {
  let comp: PublisherUpdateComponent;
  let fixture: ComponentFixture<PublisherUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let publisherFormService: PublisherFormService;
  let publisherService: PublisherService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PublisherUpdateComponent],
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
      .overrideTemplate(PublisherUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PublisherUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    publisherFormService = TestBed.inject(PublisherFormService);
    publisherService = TestBed.inject(PublisherService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const publisher: IPublisher = { id: 9111 };

      activatedRoute.data = of({ publisher });
      comp.ngOnInit();

      expect(comp.publisher).toEqual(publisher);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPublisher>>();
      const publisher = { id: 14789 };
      jest.spyOn(publisherFormService, 'getPublisher').mockReturnValue(publisher);
      jest.spyOn(publisherService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ publisher });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: publisher }));
      saveSubject.complete();

      // THEN
      expect(publisherFormService.getPublisher).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(publisherService.update).toHaveBeenCalledWith(expect.objectContaining(publisher));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPublisher>>();
      const publisher = { id: 14789 };
      jest.spyOn(publisherFormService, 'getPublisher').mockReturnValue({ id: null });
      jest.spyOn(publisherService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ publisher: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: publisher }));
      saveSubject.complete();

      // THEN
      expect(publisherFormService.getPublisher).toHaveBeenCalled();
      expect(publisherService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPublisher>>();
      const publisher = { id: 14789 };
      jest.spyOn(publisherService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ publisher });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(publisherService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
