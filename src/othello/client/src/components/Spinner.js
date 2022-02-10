import React, { Component } from 'react';

import './Spinner.css';

class Spinner extends Component {
  render() {
    if (this.props.active) {
      return (
        <div className="Spinner">
          <div className="Spinner Bounce-1"></div>
          <div className="Spinner Bounce-2"></div>
          <div className="Spinner Bounce-3"></div>
        </div>
      );
    } else {
      return <div className="Spinner"></div>;
    }
  }
}

export default Spinner;