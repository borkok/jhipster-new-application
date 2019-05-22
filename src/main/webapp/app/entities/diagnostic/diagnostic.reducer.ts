import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDiagnostic, defaultValue } from 'app/shared/model/diagnostic.model';

export const ACTION_TYPES = {
  FETCH_DIAGNOSTIC_LIST: 'diagnostic/FETCH_DIAGNOSTIC_LIST',
  FETCH_DIAGNOSTIC: 'diagnostic/FETCH_DIAGNOSTIC',
  CREATE_DIAGNOSTIC: 'diagnostic/CREATE_DIAGNOSTIC',
  UPDATE_DIAGNOSTIC: 'diagnostic/UPDATE_DIAGNOSTIC',
  DELETE_DIAGNOSTIC: 'diagnostic/DELETE_DIAGNOSTIC',
  RESET: 'diagnostic/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDiagnostic>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type DiagnosticState = Readonly<typeof initialState>;

// Reducer

export default (state: DiagnosticState = initialState, action): DiagnosticState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DIAGNOSTIC_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DIAGNOSTIC):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DIAGNOSTIC):
    case REQUEST(ACTION_TYPES.UPDATE_DIAGNOSTIC):
    case REQUEST(ACTION_TYPES.DELETE_DIAGNOSTIC):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DIAGNOSTIC_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DIAGNOSTIC):
    case FAILURE(ACTION_TYPES.CREATE_DIAGNOSTIC):
    case FAILURE(ACTION_TYPES.UPDATE_DIAGNOSTIC):
    case FAILURE(ACTION_TYPES.DELETE_DIAGNOSTIC):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DIAGNOSTIC_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_DIAGNOSTIC):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DIAGNOSTIC):
    case SUCCESS(ACTION_TYPES.UPDATE_DIAGNOSTIC):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DIAGNOSTIC):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/diagnostics';

// Actions

export const getEntities: ICrudGetAllAction<IDiagnostic> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_DIAGNOSTIC_LIST,
  payload: axios.get<IDiagnostic>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IDiagnostic> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DIAGNOSTIC,
    payload: axios.get<IDiagnostic>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDiagnostic> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DIAGNOSTIC,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDiagnostic> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DIAGNOSTIC,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDiagnostic> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DIAGNOSTIC,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
