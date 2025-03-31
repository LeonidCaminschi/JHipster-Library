import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPublisher } from '../publisher.model';
import { PublisherService } from '../service/publisher.service';

const publisherResolve = (route: ActivatedRouteSnapshot): Observable<null | IPublisher> => {
  const id = route.params.id;
  if (id) {
    return inject(PublisherService)
      .find(id)
      .pipe(
        mergeMap((publisher: HttpResponse<IPublisher>) => {
          if (publisher.body) {
            return of(publisher.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default publisherResolve;
