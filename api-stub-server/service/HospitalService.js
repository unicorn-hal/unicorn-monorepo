'use strict';


/**
 * 病院一覧取得
 * 病院一覧を取得します
 *
 * xUID String 
 * returns inline_response_200_4
 **/
exports.hospitalsGET = function (xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "data": [{
        "address": "東京都新宿区1-1-1",
        "phoneNumber": "09012345678",
        "hospitalID": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
        "postalCode": "1234567",
        "hospitalName": "東京都立大学病院"
      }]
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 病院情報取得
 * 病院情報を取得します
 *
 * xUID String 
 * hospitalID String 
 * returns Hospital
 **/
exports.hospitalsHospitalIDGET = function (xUID, hospitalID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "address": "東京都新宿区1-1-1",
      "phoneNumber": "09012345678",
      "hospitalID": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "postalCode": "1234567",
      "hospitalName": "東京都立大学病院"
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 病院ニュース一覧取得
 * 病院のニュース一覧を取得します
 *
 * xUID String 
 * hospitalID String 
 * returns inline_response_200_5
 **/
exports.hospitalsHospitalIDNewsGET = function (xUID, hospitalID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "data": [{
        "noticeImageUrl": "https://placehold.jp/150x150.png",
        "hospitalNewsID": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
        "hospitalID": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
        "contents": "新型コロナウイルスの感染拡大に伴い、当院では以下の対策を行っております。",
        "relatedUrl": "https://www.hospital.com/news/1",
        "title": "新型コロナウイルス対策について",
        "postedDate": "2020-01-01 00:00:00 +09:00"
      }]
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 病院ニュース削除
 * 病院のニュースを削除します
 *
 * xUID String 
 * hospitalID String 
 * newsID String 
 * no response value expected for this operation
 **/
exports.hospitalsHospitalIDNewsNewsIDDELETE = function (xUID, hospitalID, newsID) {
  return new Promise(function (resolve, reject) {
    resolve();
  });
}


/**
 * 病院ニュース登録
 * 病院のニュースを登録します
 *
 * body HospitalNewsRequest 
 * hospitalID String 
 * xUID String 
 * returns HospitalNewsResponse
 **/
exports.hospitalsHospitalIDNewsPOST = function (body, hospitalID, xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "noticeImageUrl": "https://placehold.jp/150x150.png",
      "hospitalID": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "contents": "新型コロナウイルスの感染拡大に伴い、当院では以下の対策を行っております。",
      "relatedUrl": "https://www.hospital.com/news/1",
      "title": "新型コロナウイルス対策について"
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

