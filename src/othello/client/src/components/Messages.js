import React, { Component } from 'react';

import './Messages.css';

var classNames = require('classnames');

class Messages extends Component {
  message() {
    switch (this.props.result) {
      case "player-one-won": return this.props.p1Name + " Won!";
      case "player-one-won-due-to-error": return this.props.p1Name + " won due to an error!";
      case "player-two-won": return this.props.p2Name + " Won!";
      case "player-two-won-due-to-error": return this.props.p2Name + " won due to an error!";
      case "tied": return "It's a tie game!";
      default: return null;
    }
  }

  winnerClassName() {
    switch (this.props.result) {
      case "player-one-won": return "Messages-player-one";
      case "player-one-won-due-to-error": return "Messages-player-one-error";
      case "player-two-won": return "Messages-player-two";
      case "player-two-won-due-to-error": return "Messages-player-two-error";
      default: return null;
    }
  }

  render() {
    var messageClass = classNames("Messages", this.winnerClassName());
    return (
      <div className={messageClass}>
        {this.message()}
      </div>
    );
  }
}

export default Messages;