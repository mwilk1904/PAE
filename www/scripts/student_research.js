/**
 * Code JavaScript pour la page de recherches de l'étudiant
 */

$(function() {
    /**
     * Recherche utilisateurs
     */
    $('a[aria-controls="pills-users_s"]').on('shown.bs.tab', function(e) {

        /**
         * Recuperation des utilisateurs
         */
        $.post("/mobilite", {
            action: "getAllStudents"
        }).fail(function(jqXHR, textStatus, error) {
            const errorData = jqXHR.responseJSON;
            console.log(errorData.message);
        }).done(function(data) {
            $('#table_users_s').DataTable({
                "language": {
                    sUrl: "French.json"
                },
                "destroy": true,
                "info": false,
                "aaData": data,
                "columns": [
                    { "data": "pseudo" },
                    { "data": "lastName" },
                    { "data": "firstName" },
                    { "data": "email" }
                ]
            });
        });
    });


    /**
     * Recherche mobilities
     */
    $('a[aria-controls="pills-mobilities"]').on('shown.bs.tab', function(e) {

        /**
         * Recuperation des utilisateurs
         */
        $.post("/mobilite", {
            action: "getAllMobilitiesOverview"
        }).fail(function(jqXHR, textStatus, error) {
            const errorData = jqXHR.responseJSON;
            console.log(errorData.message);
        }).done(function(data) {
            $('#table_mobilities_s').DataTable({
                "language": {
                    sUrl: "French.json"
                },
                "destroy": true,
                "info": false,
                "aaData": data,
                "columns": [
                    { "data": "applicationNumber" },
                    { "data": "firstName" },
                    { "data": "lastName" },
                    { "data": "preferenceOrderNumber" },
                    { "data": "mobilityCode" },
                    {
                        "data": "state",
                        render: function(data, type, row) {
                            return etatMob[data];
                        }
                    },
                    {
                        "data": "partnerId",
                        render: function(data, type, row) {
                            if (partnerIdToPartner(data) === undefined) {
                                return "";
                            }
                            return partnerIdToPartner(data).fullName;
                        }
                    },
                    {
                        "data": "countryId",
                        render: function(data, type, row) {
                            if (countryIdToCountry(data) === undefined) {
                                return "";
                            }
                            return countryIdToCountry(data).countryName;
                        }
                    },
                    {
                        "data": "programId",
                        render: function(data, type, row) {
                            if (programIdToProgram(data) === undefined) {
                                return "";
                            }
                            return programIdToProgram(data).programName;
                        }
                    }
                ],
                createdRow: function(row, data, dataIndex) {
                    if (data.state === "cancelled") {
                        $(row).addClass("cancelledRow text-danger");
                    }
                }
            });
        });
    });


    /**
     * Recherche partenaires
     */
    $('a[aria-controls="pills-partners_s"]').on('shown.bs.tab', function(e) {
        if (!$.isEmptyObject(dataTablePartners)) {
            dataTablePartners.destroy();
        }
        dataTablePartners = initPartnersTable(tabPartners);

    });



});