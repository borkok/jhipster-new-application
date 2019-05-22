export interface IDiagnostic {
  id?: number;
  name?: string;
  description?: string;
}

export const defaultValue: Readonly<IDiagnostic> = {};
