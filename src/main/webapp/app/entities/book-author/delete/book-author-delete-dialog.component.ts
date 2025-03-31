import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBookAuthor } from '../book-author.model';
import { BookAuthorService } from '../service/book-author.service';

@Component({
  templateUrl: './book-author-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BookAuthorDeleteDialogComponent {
  bookAuthor?: IBookAuthor;

  protected bookAuthorService = inject(BookAuthorService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bookAuthorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
