import { IDiagnostic } from 'app/shared/model/diagnostic.model';
import { IProfile } from 'app/shared/model/profile.model';

export const enum Unit {
  DAYS = 'DAYS'
}

export interface IHowOften {
  id?: number;
  count?: number;
  unit?: Unit;
  diagnostic?: IDiagnostic;
  profile?: IProfile;
}

export const defaultValue: Readonly<IHowOften> = {};
