import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IClient } from '../client.model';

@Component({
  selector: 'jhi-client-detail',
  templateUrl: './client-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ClientDetailComponent {
  client = input<IClient | null>(null);

  previousState(): void {
    window.history.back();
  }
}
