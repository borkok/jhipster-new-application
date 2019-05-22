import { Moment } from 'moment';
import { IDiagnostic } from 'app/shared/model/diagnostic.model';
import { IPerson } from 'app/shared/model/person.model';

export interface IExecution {
  id?: number;
  planned?: Moment;
  done?: Moment;
  doneComment?: string;
  diagnostic?: IDiagnostic;
  person?: IPerson;
}

export const defaultValue: Readonly<IExecution> = {};
