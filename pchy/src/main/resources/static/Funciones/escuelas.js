const datos = {

IPN:[
"ESCOM","UPIITA","ESIME Zacatenco",
"ESIME Culhuacán","UPIIZ","UPIICSA"
],

UNAM:[
"Facultad de Ingeniería",
"Facultad de Ciencias",
"FES Aragón",
"FES Acatlán",
"FES Cuautitlán"
],

TecNM:[
"IT Zacatecas",
"IT León",
"IT Tijuana",
"IT Durango"
],

UAM:[
"Azcapotzalco",
"Iztapalapa",
"Cuajimalpa",
"Lerma",
"Xochimilco"
]

};

function actualizarCampus(){

let uni = document.getElementById("universidad").value;
let box = document.getElementById("campusBox");
let select = document.getElementById("campus");

select.innerHTML = "";

if(datos[uni]){

box.style.display = "block";

datos[uni].forEach(item => {

let op = document.createElement("option");
op.value = item;
op.text = item;
select.appendChild(op);

});

}else{
box.style.display = "none";
}

}

function prepararInstitucion(){

let uni = document.getElementById("universidad").value;
let campus = document.getElementById("campus").value;

if(campus){
document.getElementById("institucionFinal").value = campus;
}else{
document.getElementById("institucionFinal").value = uni;
}

}