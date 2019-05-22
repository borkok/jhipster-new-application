import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './how-often.reducer';
import { IHowOften } from 'app/shared/model/how-often.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHowOftenProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class HowOften extends React.Component<IHowOftenProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { howOftenList, match } = this.props;
    return (
      <div>
        <h2 id="how-often-heading">
          How Oftens
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new How Often
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Count</th>
                <th>Unit</th>
                <th>Diagnostic</th>
                <th>Profile</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {howOftenList.map((howOften, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${howOften.id}`} color="link" size="sm">
                      {howOften.id}
                    </Button>
                  </td>
                  <td>{howOften.count}</td>
                  <td>{howOften.unit}</td>
                  <td>{howOften.diagnostic ? <Link to={`diagnostic/${howOften.diagnostic.id}`}>{howOften.diagnostic.id}</Link> : ''}</td>
                  <td>{howOften.profile ? <Link to={`profile/${howOften.profile.id}`}>{howOften.profile.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${howOften.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${howOften.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${howOften.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ howOften }: IRootState) => ({
  howOftenList: howOften.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(HowOften);
