import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IHowOften, defaultValue } from 'app/shared/model/how-often.model';

export const ACTION_TYPES = {
  FETCH_HOWOFTEN_LIST: 'howOften/FETCH_HOWOFTEN_LIST',
  FETCH_HOWOFTEN: 'howOften/FETCH_HOWOFTEN',
  CREATE_HOWOFTEN: 'howOften/CREATE_HOWOFTEN',
  UPDATE_HOWOFTEN: 'howOften/UPDATE_HOWOFTEN',
  DELETE_HOWOFTEN: 'howOften/DELETE_HOWOFTEN',
  RESET: 'howOften/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IHowOften>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type HowOftenState = Readonly<typeof initialState>;

// Reducer

export default (state: HowOftenState = initialState, action): HowOftenState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_HOWOFTEN_LIST):
    case REQUEST(ACTION_TYPES.FETCH_HOWOFTEN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_HOWOFTEN):
    case REQUEST(ACTION_TYPES.UPDATE_HOWOFTEN):
    case REQUEST(ACTION_TYPES.DELETE_HOWOFTEN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_HOWOFTEN_LIST):
    case FAILURE(ACTION_TYPES.FETCH_HOWOFTEN):
    case FAILURE(ACTION_TYPES.CREATE_HOWOFTEN):
    case FAILURE(ACTION_TYPES.UPDATE_HOWOFTEN):
    case FAILURE(ACTION_TYPES.DELETE_HOWOFTEN):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_HOWOFTEN_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_HOWOFTEN):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_HOWOFTEN):
    case SUCCESS(ACTION_TYPES.UPDATE_HOWOFTEN):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_HOWOFTEN):
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

const apiUrl = 'api/how-oftens';

// Actions

export const getEntities: ICrudGetAllAction<IHowOften> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_HOWOFTEN_LIST,
  payload: axios.get<IHowOften>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IHowOften> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_HOWOFTEN,
    payload: axios.get<IHowOften>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IHowOften> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_HOWOFTEN,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IHowOften> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_HOWOFTEN,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IHowOften> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_HOWOFTEN,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
