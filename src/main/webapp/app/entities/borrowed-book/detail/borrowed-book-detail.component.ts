import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IBorrowedBook } from '../borrowed-book.model';

@Component({
  selector: 'jhi-borrowed-book-detail',
  templateUrl: './borrowed-book-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class BorrowedBookDetailComponent {
  borrowedBook = input<IBorrowedBook | null>(null);

  previousState(): void {
    window.history.back();
  }
}
