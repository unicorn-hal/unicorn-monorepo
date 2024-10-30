'use strict';

var utils = require('../utils/writer.js');
var Disease = require('../service/DiseaseService');

module.exports.diseasesFamousGET = function diseasesFamousGET (req, res, next, xUID) {
  Disease.diseasesFamousGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.diseasesGET = function diseasesGET (req, res, next, xUID, diseaseName) {
  Disease.diseasesGET(xUID, diseaseName)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
