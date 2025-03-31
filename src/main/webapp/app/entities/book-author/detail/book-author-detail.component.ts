import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IBookAuthor } from '../book-author.model';

@Component({
  selector: 'jhi-book-author-detail',
  templateUrl: './book-author-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class BookAuthorDetailComponent {
  bookAuthor = input<IBookAuthor | null>(null);

  previousState(): void {
    window.history.back();
  }
}
