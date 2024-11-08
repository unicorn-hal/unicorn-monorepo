'use strict';

var utils = require('../utils/writer.js');
var ChronicDisease = require('../service/ChronicDiseaseService');

module.exports.chronic_diseasesChronicDiseaseIDDELETE = function chronic_diseasesChronicDiseaseIDDELETE (req, res, next, xUID, chronicDiseaseID) {
  ChronicDisease.chronic_diseasesChronicDiseaseIDDELETE(xUID, chronicDiseaseID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.chronic_diseasesGET = function chronic_diseasesGET (req, res, next, xUID) {
  ChronicDisease.chronic_diseasesGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.chronic_diseasesPOST = function chronic_diseasesPOST (req, res, next, body) {
  ChronicDisease.chronic_diseasesPOST(body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
