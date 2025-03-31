import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPublisher } from '../publisher.model';

@Component({
  selector: 'jhi-publisher-detail',
  templateUrl: './publisher-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PublisherDetailComponent {
  publisher = input<IPublisher | null>(null);

  previousState(): void {
    window.history.back();
  }
}
