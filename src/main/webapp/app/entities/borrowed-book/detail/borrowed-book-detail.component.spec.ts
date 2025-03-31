import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { BorrowedBookDetailComponent } from './borrowed-book-detail.component';

describe('BorrowedBook Management Detail Component', () => {
  let comp: BorrowedBookDetailComponent;
  let fixture: ComponentFixture<BorrowedBookDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BorrowedBookDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./borrowed-book-detail.component').then(m => m.BorrowedBookDetailComponent),
              resolve: { borrowedBook: () => of({ id: 10580 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BorrowedBookDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BorrowedBookDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load borrowedBook on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BorrowedBookDetailComponent);

      // THEN
      expect(instance.borrowedBook()).toEqual(expect.objectContaining({ id: 10580 }));
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
