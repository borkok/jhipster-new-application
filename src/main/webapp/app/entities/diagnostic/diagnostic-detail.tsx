import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './diagnostic.reducer';
import { IDiagnostic } from 'app/shared/model/diagnostic.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDiagnosticDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class DiagnosticDetail extends React.Component<IDiagnosticDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { diagnosticEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Diagnostic [<b>{diagnosticEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{diagnosticEntity.name}</dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{diagnosticEntity.description}</dd>
          </dl>
          <Button tag={Link} to="/entity/diagnostic" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/diagnostic/${diagnosticEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ diagnostic }: IRootState) => ({
  diagnosticEntity: diagnostic.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DiagnosticDetail);
