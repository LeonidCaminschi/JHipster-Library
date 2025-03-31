import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PublisherDetailComponent } from './publisher-detail.component';

describe('Publisher Management Detail Component', () => {
  let comp: PublisherDetailComponent;
  let fixture: ComponentFixture<PublisherDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublisherDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./publisher-detail.component').then(m => m.PublisherDetailComponent),
              resolve: { publisher: () => of({ id: 14789 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PublisherDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PublisherDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load publisher on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PublisherDetailComponent);

      // THEN
      expect(instance.publisher()).toEqual(expect.objectContaining({ id: 14789 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
