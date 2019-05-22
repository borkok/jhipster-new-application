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
import { IProfile } from 'app/shared/model/profile.model';
import { getEntities as getProfiles } from 'app/entities/profile/profile.reducer';
import { getEntity, updateEntity, createEntity, reset } from './how-often.reducer';
import { IHowOften } from 'app/shared/model/how-often.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IHowOftenUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IHowOftenUpdateState {
  isNew: boolean;
  diagnosticId: string;
  profileId: string;
}

export class HowOftenUpdate extends React.Component<IHowOftenUpdateProps, IHowOftenUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      diagnosticId: '0',
      profileId: '0',
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
    this.props.getProfiles();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { howOftenEntity } = this.props;
      const entity = {
        ...howOftenEntity,
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
    this.props.history.push('/entity/how-often');
  };

  render() {
    const { howOftenEntity, diagnostics, profiles, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="jhipsterNewApplicationApp.howOften.home.createOrEditLabel">Create or edit a HowOften</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : howOftenEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="how-often-id">ID</Label>
                    <AvInput id="how-often-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="countLabel" for="how-often-count">
                    Count
                  </Label>
                  <AvField
                    id="how-often-count"
                    type="string"
                    className="form-control"
                    name="count"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="unitLabel" for="how-often-unit">
                    Unit
                  </Label>
                  <AvInput
                    id="how-often-unit"
                    type="select"
                    className="form-control"
                    name="unit"
                    value={(!isNew && howOftenEntity.unit) || 'DAYS'}
                  >
                    <option value="DAYS">DAYS</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="how-often-diagnostic">Diagnostic</Label>
                  <AvInput id="how-often-diagnostic" type="select" className="form-control" name="diagnostic.id">
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
                  <Label for="how-often-profile">Profile</Label>
                  <AvInput id="how-often-profile" type="select" className="form-control" name="profile.id">
                    <option value="" key="0" />
                    {profiles
                      ? profiles.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/how-often" replace color="info">
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
  profiles: storeState.profile.entities,
  howOftenEntity: storeState.howOften.entity,
  loading: storeState.howOften.loading,
  updating: storeState.howOften.updating,
  updateSuccess: storeState.howOften.updateSuccess
});

const mapDispatchToProps = {
  getDiagnostics,
  getProfiles,
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
)(HowOftenUpdate);
