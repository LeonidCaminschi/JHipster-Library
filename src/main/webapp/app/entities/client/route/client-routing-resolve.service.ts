import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClient } from '../client.model';
import { ClientService } from '../service/client.service';

const clientResolve = (route: ActivatedRouteSnapshot): Observable<null | IClient> => {
  const id = route.params.id;
  if (id) {
    return inject(ClientService)
      .find(id)
      .pipe(
        mergeMap((client: HttpResponse<IClient>) => {
          if (client.body) {
            return of(client.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default clientResolve;
