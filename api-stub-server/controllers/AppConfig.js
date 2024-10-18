'use strict';

var utils = require('../utils/writer.js');
var AppConfig = require('../service/AppConfigService');

module.exports.app_configGET = function app_configGET (req, res, next, xUID) {
  AppConfig.app_configGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
