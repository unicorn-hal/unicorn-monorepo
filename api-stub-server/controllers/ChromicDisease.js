'use strict';

var utils = require('../utils/writer.js');
var ChromicDisease = require('../service/ChromicDiseaseService');

module.exports.chronic_diseasesChronicDiseaseIDDELETE = function chronic_diseasesChronicDiseaseIDDELETE (req, res, next, xUID, chronicDiseaseID) {
  ChromicDisease.chronic_diseasesChronicDiseaseIDDELETE(xUID, chronicDiseaseID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.chronic_diseasesGET = function chronic_diseasesGET (req, res, next, xUID) {
  ChromicDisease.chronic_diseasesGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.chronic_diseasesPOST = function chronic_diseasesPOST (req, res, next, body) {
  ChromicDisease.chronic_diseasesPOST(body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
