import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IDiagnostic } from 'app/shared/model/diagnostic.model';
import { getEntities as getDiagnostics } from 'app/entities/diagnostic/diagnostic.reducer';
import { IPerson } from 'app/shared/model/person.model';
import { getEntities as getPeople } from 'app/entities/person/person.reducer';
import { getEntity, updateEntity, createEntity, reset } from './execution.reducer';
import { IExecution } from 'app/shared/model/execution.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IExecutionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IExecutionUpdateState {
  isNew: boolean;
  diagnosticId: string;
  personId: string;
}

export class ExecutionUpdate extends React.Component<IExecutionUpdateProps, IExecutionUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      diagnosticId: '0',
      personId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getDiagnostics();
    this.props.getPeople();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { executionEntity } = this.props;
      const entity = {
        ...executionEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/execution');
  };

  render() {
    const { executionEntity, diagnostics, people, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="jhipsterNewApplicationApp.execution.home.createOrEditLabel">Create or edit a Execution</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : executionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="execution-id">ID</Label>
                    <AvInput id="execution-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="plannedLabel" for="execution-planned">
                    Planned
                  </Label>
                  <AvField
                    id="execution-planned"
                    type="date"
                    className="form-control"
                    name="planned"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="doneLabel" for="execution-done">
                    Done
                  </Label>
                  <AvField id="execution-done" type="date" className="form-control" name="done" />
                </AvGroup>
                <AvGroup>
                  <Label id="doneCommentLabel" for="execution-doneComment">
                    Done Comment
                  </Label>
                  <AvField id="execution-doneComment" type="text" name="doneComment" />
                </AvGroup>
                <AvGroup>
                  <Label for="execution-diagnostic">Diagnostic</Label>
                  <AvInput id="execution-diagnostic" type="select" className="form-control" name="diagnostic.id">
                    <option value="" key="0" />
                    {diagnostics
                      ? diagnostics.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="execution-person">Person</Label>
                  <AvInput id="execution-person" type="select" className="form-control" name="person.id">
                    <option value="" key="0" />
                    {people
                      ? people.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/execution" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  diagnostics: storeState.diagnostic.entities,
  people: storeState.person.entities,
  executionEntity: storeState.execution.entity,
  loading: storeState.execution.loading,
  updating: storeState.execution.updating,
  updateSuccess: storeState.execution.updateSuccess
});

const mapDispatchToProps = {
  getDiagnostics,
  getPeople,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ExecutionUpdate);
