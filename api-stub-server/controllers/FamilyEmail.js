'use strict';

var utils = require('../utils/writer.js');
var FamilyEmail = require('../service/FamilyEmailService');

module.exports.family_emailsFamilyEmailIDDELETE = function family_emailsFamilyEmailIDDELETE (req, res, next, xUID, familyEmailID) {
  FamilyEmail.family_emailsFamilyEmailIDDELETE(xUID, familyEmailID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.family_emailsFamilyEmailIDPUT = function family_emailsFamilyEmailIDPUT (req, res, next, body, familyEmailID, xUID) {
  FamilyEmail.family_emailsFamilyEmailIDPUT(body, familyEmailID, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.family_emailsGET = function family_emailsGET (req, res, next, xUID) {
  FamilyEmail.family_emailsGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.family_emailsPOST = function family_emailsPOST (req, res, next, body, xUID) {
  FamilyEmail.family_emailsPOST(body, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
