'use strict';


/**
 * おくすり一覧取得
 * 登録してある薬の名前と残量を取得します
 *
 * xUID String 
 * returns inline_response_200_7
 **/
exports.medicinesGET = function(xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "data" : [ {
    "quantity" : 20,
    "medicineID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "count" : 30,
    "medicineName" : "パラセタモール"
  }, {
    "quantity" : 20,
    "medicineID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "count" : 30,
    "medicineName" : "パラセタモール"
  } ]
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * おくすり削除
 * 登録済みの薬を削除します
 *
 * xUID String 
 * medicineID String 
 * no response value expected for this operation
 **/
exports.medicinesMedicineIDDELETE = function(xUID,medicineID) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * おくすり更新
 * 登録済みの薬の情報を更新します
 *
 * body MedicineRequest 
 * medicineID String 
 * xUID String 
 * returns MedicineResponse
 **/
exports.medicinesMedicineIDPUT = function(body,medicineID,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "quantity" : 20,
  "medicineID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "count" : 30,
  "medicineName" : "パラセタモール"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * おくすり追加
 * 新しい薬を追加します
 *
 * body MedicinePostRequest 
 * xUID String 
 * returns MedicineResponse
 **/
exports.medicinesPOST = function(body,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "quantity" : 20,
  "medicineID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "count" : 30,
  "medicineName" : "パラセタモール"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

