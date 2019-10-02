/**
 * Code JavaScript pour la page dde données personnelles de l'étudiant
 */
let personalDataValidator = null;

$(function() {

    /**
     * Validateur de formulaire pour l'inscription
     */
    personalDataValidator = initPersonalDataValidator();

    $('#pers_data_birthDate').datepicker({
        language: "fr"
    });
    /**
     * Gestion de la confirmation des données personnelles
     */
    $('#bt_confirm_pers_data').on('click', function() {

        let length = $('.formPersonalData').bootstrapValidator('validate').has('.has-error').length; // = 0 si le formulaire est valide

        if (personalDataValidator != null && length === 0) {

            let formDatas = formToJSON("formPersonalData");
            let natId = countryNameToCountry(formDatas['nationality']).id;

            delete formDatas['nationality'];
            formDatas['nationalityId'] = natId;
            let splitDate = formDatas['birthDate'].split('/');
            let date = new Date(splitDate[2], splitDate[1], splitDate[0])

            formDatas['birthDate'] = date.getTime();
            if (formDatas['sex'] === "Sexe") formDatas['sex'] = ' ';
            $.each(formDatas, function(key, value) {
                if (key === "numberHighSchollYearsSucceded" || key === "zipCode" || key === "streetNumber") {
                    studentPersData[key] = parseInt(value);
                    return;
                }
                studentPersData[key] = value;
            });

            studentPersData['numVersion'] += 1;
            $.post("/mobilite", {
                action: "updateStudentData",
                data: JSON.stringify(studentPersData)
            }).fail(function(jqXHR, textStatus, error) {
                const errorData = jqXHR.responseJSON;
                console.log(errorData.message);
            });
            alertify.success("Vous avez mis à jour vos données personnelles!");

        }
    });

    $('a[aria-controls="personal_data"]').on('hide.bs.tab', function(e) {
        $('.formPersonalData').data('bootstrapValidator').resetForm();
        $('.formPersonalData').bootstrapValidator('destroy');
        $('.formPersonalData').trigger("reset");
        $("#pers_data_nationality").val(null).trigger("change");
        personalDataValidator = initPersonalDataValidator();
    });

    $('a[aria-controls="personal_data"]').on('shown.bs.tab', function(e) {

        $.post("/mobilite", {
            action: "getStudentData"
        }).fail(function(jqXHR, textStatus, error) {
            const errorData = jqXHR.responseJSON;
            console.log(errorData.message);
        }).done(function(data) {
            studentPersData = data;
            setTimeout(function() {
                JSONtoForm('#personal_data', data);
            }, 300)
        });
    });

});

function fillNationalitySelect() {
    if ($('#pers_data_nationality').length === tabCountries + 1 || $('#nv_partner_country').length === tabCountries + 1) {
        return;
    }
    for (let i = 0; i < tabCountries.length; i++) {
        $('#pers_data_nationality , #nv_partner_country').append($('<option>', {
            text: tabCountries[i].countryName,
            value: tabCountries[i].countryName
        }));
    }

    setTimeout(function() {
        $('#pers_data_nationality').select2({
            placeholder: "Votre Nationalité"
        });
    }, 500);

    setTimeout(function() {
        $('#nv_partner_country').select2({
            placeholder: "Pays de l'entreprise",
            allowClear: true
        });
    }, 500);
}
/**
 * Remplissage des champs d'un formulaire apd d'un JSON
 * @param {*} formId 
 * @param {*} data 
 */
function JSONtoForm(formId, data) {
    $.each(data, function(key, value) {
        if (value) {
            if (key === "nationalityId") {
                key = "nationality";
            }

            let input = $('[name=' + key + ']', formId);

            if (key === 'birthDate') {
                input.val((new Date(value)).toLocaleDateString("fr-FR"));
                return;
            }
            if (key === "nationality") {
                value = countryIdToCountry(value).countryName;
                $('#pers_data_nationality').val(value);
                $('#pers_data_nationality').trigger('change');
                return;
            }
            if (input.is('select')) {
                if (value === ' ') {
                    return;
                }
                input.find('option[value="' + value + '"]').attr('selected', true);
                return;
            }
            input.val(value);
        }
    });
}


/**
 * Initialisation du validator pour les données personnlles
 */
function initPersonalDataValidator() {
    return $('.formPersonalData').bootstrapValidator({
        fields: {
            title: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    }
                }
            },
            lastName: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    }
                }
            },
            firstName: {
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
            birthDate: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    }
                }
            },
            nationality: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    }
                }
            },
            street: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    }
                }
            },
            streetNumber: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
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
            numberHighSchollYearsSucceded: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    }
                }
            },
            bankCardNumber: {
                validators: {
                    regexp: {
                        regexp: /^BE ?\d{2}( ?\d{4}){3}$/i,
                        message: 'Le format de l\' iban n\'est pas conforme (Format belge)'
                    }
                }
            }
        }
    });
}