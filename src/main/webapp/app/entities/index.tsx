import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Person from './person';
import Profile from './profile';
import HowOften from './how-often';
import Diagnostic from './diagnostic';
import Execution from './execution';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/person`} component={Person} />
      <ErrorBoundaryRoute path={`${match.url}/profile`} component={Profile} />
      <ErrorBoundaryRoute path={`${match.url}/how-often`} component={HowOften} />
      <ErrorBoundaryRoute path={`${match.url}/diagnostic`} component={Diagnostic} />
      <ErrorBoundaryRoute path={`${match.url}/execution`} component={Execution} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
