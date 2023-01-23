import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubmission } from '../submission.model';
import { SubmissionService } from '../service/submission.service';

@Injectable({ providedIn: 'root' })
export class SubmissionRoutingResolveService implements Resolve<ISubmission | null> {
  constructor(protected service: SubmissionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISubmission | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((submission: HttpResponse<ISubmission>) => {
          if (submission.body) {
            return of(submission.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
