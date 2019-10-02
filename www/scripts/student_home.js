/**
 * Code JavaScript pour la page d'accueil de l'étudiant
 */


/**
 * Traduction des différents états d'une mobilité
 */
const etatMob = {
    none: "Aucun",
    created: "Créée",
    cancelled: "Annulée",
    in_preparation: "En préparation",
    to_pay: "À payer",
    to_pay_balance: "À payer solde",
    switzerland: "En Suisse",
    switzerland_post: "Retour de Suisse",
    first_demand: "1ere demande de paiement",
    second_demand: "2eme demande de paiement"
}


let tabPartners = {};
let tabCountries = {};
let tabPrograms = {};
let tabMobilities = {};
let compteurSMS = 0;
let dataTable = {};
let studentPersData = {};
let nvPartnerDataValidator = {};

handleErrors();

$(function() {

    nvPartnerDataValidator = initNvPartnerDataValidator();

    /**
     * Parametres perso de alertify
     */
    alertify.defaults.transition = "zoom";
    alertify.defaults.theme.ok = "btn btn-primary";
    alertify.defaults.theme.cancel = "btn btn-danger";
    alertify.defaults.theme.input = "form-control";

    /**
     * Executé a la fin de toutes les requetes ajax
     */
    $(document).ajaxStop(function() {
        $('#choixSMS1').select2();
        $('#choixSMP1').select2();

    });

    fillNationalitySelect();

    $('#bt_nv_partner').on('click', function() {
        $('#nv_partner-tab').click();
    });
    $('#bt_back_nv_partner').on('click', function() {
        $('a[aria-controls=home]').click();
    });

    $('a[aria-controls=nv_partner]').on('hide.bs.tab', function() {
        //$('.formNvPartner').bootstrapValidator('resetForm', true);
        $('.formNvPartner').data('bootstrapValidator').resetForm();
        $('.formNvPartner').bootstrapValidator('destroy');
        $('.formNvPartner').trigger("reset");
        $("#nv_partner_country").val(null).trigger("change");
        nvPartnerDataValidator = initNvPartnerDataValidator();
        //$("#nv_partner_country").val("1").trigger("change");


    });




    /**
     * Gestion du bouton de déconnexion
     */
    $('#bt_logout_student').on("click", function(event) {
        if (teacherIsConnectedAsStudent) {
            /**
             * Revenir en tant que prof
             */
            $.post("/mobilite", {
                action: "backToTeacher"
            }).fail(function(jqXHR, textStatus, error) {
                const errorData = jqXHR.responseJSON;
                console.log(errorData.message);
            }).done(function() {
                alertify.success("Vous etes de nouveau connecté en tant que " + teacher.pseudo);
                $('#bt_logout_student').text("Déconnexion");
                loggedOut();
                loggedIn_teacher(teacher);
                teacher = null;
                teacherIsConnectedAsStudent = false;
            });

        }
        if (whoami.role === 'student') {
            studentPersData = [];
        }
        event.preventDefault(); // empêche de suivre l'hyperlien
        $.post("/mobilite", {
            action: "logout"
        });
        loggedOut();
    });

    /**
     * Gestion du bouton de nouveau choix SMS
     */
    $('#add_choice_SMS').on('click', function() {
        compteurSMS++;
        if (compteurSMS >= 3) {
            alertify.warning('Vous pouvez déclarer seulement 3 mobilités SMS !');
            return;
        }
        $('#formSMS .selectSMS').first().select2("destroy");
        $('#formSMS .selectSMS').first().removeAttr('data-select2-id');
        var noOfDivs = $('.divSMS').length;
        var clonedDiv = $('.divSMS').first().clone(true);
        clonedDiv.find('select').attr('id', "choixSMS" + (noOfDivs + 1));
        clonedDiv.insertBefore("#placeholderSMS");
        clonedDiv.attr('id', 'divSMS' + noOfDivs);
        clonedDiv.find('label').text((clonedDiv.index() + 1) + ")")

        $('#formSMS .selectSMS').select2();
        $('#formSMS .selectSMS').first().select2();
    });

    /**
     * Gestion du bouton de nouveau choix SMP
     */
    $('#add_choice_SMP').on('click', function() {
        $('#formSMP .selectSMP').first().select2("destroy");
        $('#formSMP .selectSMP').first().removeAttr('data-select2-id');
        var noOfDivs = $('.divSMP').length;
        var clonedDiv = $('.divSMP').first().clone(true);
        clonedDiv.find('select').attr('id', "choixSMP" + (noOfDivs + 1));
        clonedDiv.insertBefore("#placeholderSMP");
        clonedDiv.attr('id', 'divSMP' + noOfDivs);
        clonedDiv.find('label').text((clonedDiv.index() + 1) + ")")
        $('#formSMP .selectSMP').select2();
        $('#formSMP .selectSMP').first().select2();
    });


    /**
     * Gestion du bouton de validation des choix
     */
    $('#bt_check_choices').on('click', function() {
        let choices = serializeChoicesForm();
        if (choices.length === 0) {
            alertify.warning('Veuillez déclarer au moins un choix');
            return;
        }
        alertify.confirm('Confirmation', 'Êtes-vous sûr de vouloir valider ces choix-là?', function() {

            $.post("/mobilite", {
                action: "declareStudentMobilities",
                data: JSON.stringify(choices)
            }).fail(function(jqXHR, textStatus, error) {
                const errorData = jqXHR.responseJSON;
                console.log(errorData.message);
            }).done(function() {
                ajaxGetMobilities();
            });
            alertify.success('Choix confimé(s)!');
        }, function() { alertify.error('Choix annulé(s)') }).set('labels', { ok: 'Confirmer', cancel: 'Annuler' });

    });

    /**
     * Bouton d'annulation de toutes les demandes
     */
    $('#bt_cancel_request').on('click', function() {
        alertify.prompt('Annulation de la demande', 'Indiquez la raison de votre annulation:', '', function(evt, value) {
            if (value === '') {
                alertify.warning('Veuillez spécifier une raison');
                return;
            }
            let reason = {};
            reason['reason'] = value;
            $.post("/mobilite", {
                action: "cancelMobilities",
                data: JSON.stringify(reason)
            }).fail(function(jqXHR, textStatus, error) {
                const errorData = jqXHR.responseJSON;
                console.log(errorData.message);
            }).done(function() {
                ajaxGetMobilities();
            });
            alertify.success('La raison est : ' + value)
        }, function() {}).set('labels', { ok: 'Confirmer', cancel: 'Annuler' });
    });


    /**
     * Details de la table d'avancements
     */

    // Array to track the ids of the details displayed rows
    let detailRows = [];
    $('#tb_advancements tbody').on('click', 'tr td', function() {
        var tr = $(this).closest('tr');
        var row = dataTable.row(tr);
        var idx = $.inArray(tr.attr('id'), detailRows);

        if (row.child.isShown()) {
            tr.removeClass('details');
            row.child.hide();

            // Remove from the 'open' array
            detailRows.splice(idx, 1);
        } else {
            tr.addClass('details');
            row.child(formatTableMob(row.data())).show();

            // Add to the 'open' array
            if (idx === -1) {
                detailRows.push(tr.attr('id'));
            }
        }
    });
    /**
     * Confirmation du nouveau partenaire
     */
    $('#bt_confirm_nv_partner').on('click', function() {

        let length = $('.formNvPartner').bootstrapValidator('validate').has('.has-error').length; // = 0 si le formulaire est valide

        if (nvPartnerDataValidator != null && length === 0) {

            let formDatas = formToJSON("formNvPartner");
            if (countryNameToCountry(formDatas['country']) === undefined) {
                alertify.error("Entrez un pays");
                return false;
            }
            let countryId = countryNameToCountry(formDatas['country']).id;

            delete formDatas['country'];
            formDatas['countryId'] = countryId;
            formDatas['id'] = 0;

            $.post("/mobilite", {
                action: "addPartner",
                data: JSON.stringify(formDatas)
            }).fail(function(jqXHR, textStatus, error) {
                const errorData = jqXHR.responseJSON;
                console.log(errorData.message);
            }).done(function() {
                alertify.success('Vous avez ajouté un nouveau partenaire');
                location.reload();
            });

        }

    });

});

/**
 * Appels ajax a la connexion de l'etudiant 
 */
function ajaxCallsStudentConnection() {
    /**
     * Recuperation des programmes
     */
    $.post("/mobilite", {
            action: "getPrograms"

        },
        function(resp) {
            tabPrograms = resp;
        }
    ).fail(function(jqXHR, textStatus, error) {
        const errorData = jqXHR.responseJSON;
        console.log(errorData.message);
    }).done(function(resp) {
        if ($('#choixSMS1 option.program').length === tabPrograms.length) {
            return;
        }
        for (let i = 0; i < resp.length; i++) {
            let idSMS = 'opt_' + resp[i].programName + '_sms';
            let idSMP = 'opt_' + resp[i].programName + '_smp';
            if (resp[i].programName === "Erasmus+") {
                idSMS = 'opt_Erasmus_sms';
                idSMP = 'opt_Erasmus_smp';
            }
            $('#choixSMS1').append($('<option>', {
                id: idSMS,
                text: "Programme: " + resp[i].programName,
                value: "programId " + resp[i].id,
                class: "font-italic program"

            }));
            $('#choixSMP1').append($('<option>', {
                id: idSMP,
                text: "Programme: " + resp[i].programName,
                value: "programId " + resp[i].id
            }));
        }

    });


    /**
     * Recuperation des pays
     */
    $.post("/mobilite", {
            action: "getCountries"

        },
        function(resp) {
            tabCountries = resp;
            fillNationalitySelect();
        }
    ).fail(function(jqXHR, textStatus, error) {
        const errorData = jqXHR.responseJSON;
        console.log(errorData.message);
    }).done(function(resp) {
        setTimeout(function() {
            if ($('#choixSMS1 option.country').length === tabCountries.length) {
                return;
            }
            for (let i = 0; i < resp.length; i++) {
                if (programIdToProgram(resp[i].programId).programName === "Erasmus+") {
                    $('#opt_Erasmus_sms, #opt_Erasmus_smp').after($('<option>', {
                        text: "Pays: " + resp[i].countryName,
                        value: "countryId " + resp[i].id,
                        class: "country"
                    }));
                    continue;
                }
                if (resp[i].countryName === "Suisse") {
                    continue;
                }
                $('#opt_' + programIdToProgram(resp[i].programId).programName + '_sms, #opt_' + programIdToProgram(resp[i].programId).programName + '_smp').after($('<option>', {
                    text: "Pays: " + resp[i].countryName,
                    value: "countryId " + resp[i].id,
                    class: "country"
                }));
            }
        }, 200);
    });

    /**
     * Recuperation des partenaires
     */
    $.post("/mobilite", {
            action: "getPartners"

        },
        function(resp) {
            tabPartners = resp;
        }
    ).fail(function(jqXHR, textStatus, error) {
        const errorData = jqXHR.responseJSON;
        console.log(errorData.message);
    }).done(function(resp) {
        if ($('#choixSMS1 option.partner').length === tabPartners.length) {
            return;
        }
        for (let i = 0; i < resp.length; i++) {
            if (resp[i].mobilityCode === "SMS") { // seulement les partenaires SMS sont visibles pour l'étudiant
                $('#choixSMS1').append($('<option>', {
                    text: "Partenaire: " + resp[i].legalName,
                    value: "partnerId " + resp[i].id,
                    class: "partner"
                }));
            }
        }
    });

    setTimeout(function() {
        ajaxGetMobilities();
    }, 1000);


}

function ajaxGetMobilities() {
    if (whoami.role !== 'student') {
        return;
    }
    let JSONdata;
    /**
     * Recuperation les mobilités de l'étudiant
     */
    $.post("/mobilite", {
        action: "getStudentMobilities"
    }).fail(function(jqXHR, textStatus, error) {
        const errorData = jqXHR.responseJSON;
        console.log(errorData.message);

    }).done(function(data) {
        if (data.length === 0) {
            if (whoami.role === "student") {
                let row = $('<div>', {
                    class: "row justify-content-center mt-3 font-italic text-danger",
                    text: "Aucune demande de mobilité n'est en cours"
                });
                $('#tb_advancements').replaceWith(row);
            }
        } else {
            if (whoami.role === "student") {

                JSONdata = transformReceivedJSONMobilities(data);
                if (!$.isEmptyObject(dataTable)) {
                    dataTable.destroy();
                }
                dataTable = initMobilitiesTable(JSONdata);
                $("#bt_cancel_request").removeAttr('disabled');

                let cptSMS = 0;
                let oneIsNotCancelled = false;
                for (let i = 0; i < data.length; i++) {
                    if (data[i].mobilityCode === "SMS") {
                        cptSMS++;
                    }
                    if (data[i].state !== "none") {
                        $('a[aria-controls="personal_data"]').removeClass('disabled'); //si un étudiant a une demande confirmée, il se voit accordé l'onglet "Données personnelles"
                    }
                    if (data[i].state !== "Annulée") {

                        oneIsNotCancelled = true;
                    }
                }
                if (!oneIsNotCancelled) {
                    $("#bt_cancel_request").attr("disabled", true);
                }
                compteurSMS += cptSMS;
            }
        }
    });

    return JSONdata;
}

function partnerIdToPartner(partnerId) {
    return tabPartners.find(function(e) {
        return e.id === partnerId;
    });
}

function countryIdToCountry(countryId) {
    return tabCountries.find(function(e) {
        return e.id === countryId;
    });
}

function programIdToProgram(programId) {
    return tabPrograms.find(function(e) {
        return e.id === programId;
    });
}

function countryNameToCountry(countryName) {
    return tabCountries.find(function(e) {
        return e.countryName === countryName;
    });
}
/**
 * Recuperer les choix de l'etudiant
 */
function serializeChoicesForm() {
    let data = [];
    $("#formSMS").find('select').each(function() {
        let mobility = {};
        mobility["mobilityCode"] = "SMS";
        if ($(this).val() !== null) {
            mobility["partnerId"] = 0;
            mobility["countryId"] = 0;
            mobility["programId"] = 0;
            let tabValues = $(this).val().split(' ');
            tabValues[1] = parseInt(tabValues[1]);
            mobility[tabValues[0]] = tabValues[1];
            switch (tabValues[0]) {
                case "programId":
                    break;
                case "countryId":
                    mobility["programId"] = countryIdToCountry(tabValues[1]).programId;
                    break;
                case "partnerId":
                    let partner = partnerIdToPartner(tabValues[1]);
                    let country = countryIdToCountry(partner.countryId);
                    mobility["countryId"] = country.id;
                    mobility["programId"] = country.programId;
                    break;
                default:
                    break;
            }
            mobility[tabValues[0]] = tabValues[1];
            data.push(mobility);
        }
    });
    $("#formSMP").find('select').each(function() {
        let mobility = {};
        mobility["mobilityCode"] = "SMP";
        if ($(this).val() !== null) {
            mobility["partnerId"] = 0;
            mobility["countryId"] = 0;
            mobility["programId"] = 0;
            let tabValues = $(this).val().split(' ');
            tabValues[1] = parseInt(tabValues[1]);
            mobility[tabValues[0]] = tabValues[1];
            switch (tabValues[0]) {
                case "programId":
                    break;
                case "countryId":
                    mobility["programId"] = countryIdToCountry(tabValues[1]).programId;
                    break;
                case "partnerId":
                    let partner = partnerIdToPartner(tabValues[1]);
                    let country = countryIdToCountry(partner.countryId);
                    mobility["countryId"] = country.id;
                    mobility["programId"] = country.programId;
                    break;
                default:
                    break;
            }
            mobility[tabValues[0]] = tabValues[1];
            data.push(mobility);
        }
    });
    return data;
}

function transformReceivedJSONMobilities(dataJSON) {
    for (let i = 0; i < dataJSON.length; i++) {
        let partner = partnerIdToPartner(dataJSON[i].partnerId);
        let country = countryIdToCountry(dataJSON[i].countryId);
        dataJSON[i]['state'] = etatMob[dataJSON[i]['state']];
        dataJSON[i]['program'] = programIdToProgram(dataJSON[i]['programId']).programName;
        if (partner === undefined && country === undefined) {
            dataJSON[i]['countryName'] = "";
            dataJSON[i]['partner'] = "";
        } else if (partner === undefined) {
            dataJSON[i]['countryName'] = country.countryName;
            dataJSON[i]['partner'] = '';
        } else {
            country = countryIdToCountry(partner.countryId);
            dataJSON[i]['countryName'] = country.countryName;
            dataJSON[i]['partner'] = partner.legalName;
        }

    }
    return dataJSON;
}

function initMobilitiesTable(dataReceived) {
    let dt = $('#tb_advancements').DataTable({
        "destroy": true,
        "paging": false,
        "info": false,
        "searching": false,
        "aaData": dataReceived,
        "columns": [{
                "className": 'details-control',
                "orderable": false,
                "data": null,
                "defaultContent": ''
            },
            { "data": "preferenceOrderNumber" },
            { "data": "mobilityCode" },
            { "data": "partner" },
            { "data": "state" },
            {
                "data": "firstPayReq",
                render: function(data, type, row) {
                    if (data) {
                        return '<input class="form-check-input" type="checkbox" value="" disabled checked>';
                    } else {
                        return '<input class="form-check-input" type="checkbox" value="" disabled>';
                    }
                },
                className: "dt-body-center text-center"
            },
            {
                "data": "secondPayReq",
                render: function(data, type, row) {
                    if (data) {
                        return '<input class="form-check-input" type="checkbox" value="" disabled checked>';
                    } else {
                        return '<input class="form-check-input" type="checkbox" value="" disabled>';
                    }
                },
                className: "dt-body-center text-center"
            },
        ],
        createdRow: function(row, data, dataIndex) {
            if (data.state === "Annulée") {
                $(row).addClass("cancelledRow text-danger");
            }
        }
    });
    return dt;
}

function formatTableMob(d) {
    return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">' +
        '<tr>' +
        '<td>Programme:</td>' +
        '<td>' + programIdToProgram(d.programId).programName + '</td>' +
        '</tr>' +
        '<tr>' +
        '<td>Pays:</td>' +
        '<td>' + d.countryName + '</td>' +
        '</tr>' +
        '</table>';
}

function handleErrors() {
    window.onerror = function(error, url, line) {
        if (error.namespace.substring(0, 3) !== "bv.")
            alertify.error("Une erreur s'est produite.\n Veuillez recharger la page.");
    };
    return false;
}

/**
 * Initialisation du validator pour un nouveau partenaire
 */
function initNvPartnerDataValidator() {
    return $('.formNvPartner').bootstrapValidator({
        excluded: [':disabled'],
        fields: {
            legalName: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    }
                }
            },
            businessName: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    }
                }
            },
            fullName: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    },
                    stringLength: {
                        min: 2,
                        message: errorMessages.tooShort(2)
                    }
                }
            },
            address: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    }
                }
            },
            country: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    },
                    stringLength: {
                        min: 2,
                        message: errorMessages.tooShort(2)
                    }
                }
            },
            zipCode: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    }
                }
            },

            city: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    }
                }
            },
            email: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    },
                    emailAddress: {
                        message: errorMessages.wrongEmailFormat
                    }
                }
            },
            website: {
                validators: {
                    uri: {
                        message: errorMessages.wrongWebSiteFormat
                    }
                }
            },
            phoneNumber: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    }
                }
            }
        }
    });
}