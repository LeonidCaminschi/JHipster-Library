import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPublisher } from '../publisher.model';
import { PublisherService } from '../service/publisher.service';

@Component({
  templateUrl: './publisher-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PublisherDeleteDialogComponent {
  publisher?: IPublisher;

  protected publisherService = inject(PublisherService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.publisherService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
