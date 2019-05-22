import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './how-often.reducer';
import { IHowOften } from 'app/shared/model/how-often.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHowOftenDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class HowOftenDetail extends React.Component<IHowOftenDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { howOftenEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            HowOften [<b>{howOftenEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="count">Count</span>
            </dt>
            <dd>{howOftenEntity.count}</dd>
            <dt>
              <span id="unit">Unit</span>
            </dt>
            <dd>{howOftenEntity.unit}</dd>
            <dt>Diagnostic</dt>
            <dd>{howOftenEntity.diagnostic ? howOftenEntity.diagnostic.id : ''}</dd>
            <dt>Profile</dt>
            <dd>{howOftenEntity.profile ? howOftenEntity.profile.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/how-often" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/how-often/${howOftenEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ howOften }: IRootState) => ({
  howOftenEntity: howOften.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(HowOftenDetail);
