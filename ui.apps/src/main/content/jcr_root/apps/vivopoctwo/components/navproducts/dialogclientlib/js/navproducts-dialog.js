(function ($, Granite) {
    "use strict";

    $(document).on("foundation-contentloaded", function () {
        $('#menuData').css({ 'border-color': '#ddd', 'background': '#ddd', 'cursor': 'not-allowed' });
        $('#updatedDate').css({ 'border-color': '#ddd', 'background': '#ddd', 'cursor': 'not-allowed' });



        $('#syncMenuData').click(function () {
            var endpoint = 'https://jsonplaceholder.typicode.com/todos';
            console.log('DISPAROU A DIALOG');
            let loading = createLoading();
            $('#menuData').parent().after(loading);
            showResults(false, false);

            $.ajax({
                url: endpoint,
                contentType: "application/json",
                dataType: 'json',
                success: function (result) {
                    if (result) {
                        $('#menuData').val(JSON.stringify(result));
                        result.map(function(x){
                            let title = x.title;
                            $('#menuData3').val(title);
                        })
                        

                        showResults(true, false);
                    } else {
                        showResults(true, true);
                    }
                },
                error: function (result) {
                    showResults(true, true);
                }
            })


        });

        function showResults(hasResults, hasError) {
            if (hasResults == true && hasError == false) {
                let data = new Date();
                $('#menuData').parent().parent().find('.header-dialog__loading').remove();
                $('#menuData').parent().show();
                $('#updatedDate').val(`${data.getDate()}/${data.getMonth() + 1}/${data.getFullYear()} - ${data.getHours()}:${data.getMinutes()}:${data.getSeconds()}`);
                $('#updatedDate').parent().show();
                $(window).adaptTo('foundation-ui').alert('Successo', 'Os dados foram recuperados com sucesso! Salve a caixa de diálogo para finalizar a operação.', 'success');
            } else if (hasResults == true && hasError == true) {
                $('#menuData').parent().parent().find('.header-dialog__loading').remove();
                $('#menuData').parent().show();
                $('#updatedDate').parent().show();
                $(window).adaptTo('foundation-ui').alert('Erro', 'Não foi possível atualizar os dados no momento. Tente novamente mais tarde.', 'error');
            } else {
                $('#menuData').parent().hide();
                $('#updatedDate').parent().hide();
            }

        }

        function createLoading() {
            let loadingContainer = document.createElement('div');
            loadingContainer.classList.add('header-dialog__container');

            let loadingElement = document.createElement('div');
            loadingElement.classList.add('header-dialog__loading');
            loadingElement.append(document.createElement('div'));
            loadingElement.append(document.createElement('div'));
            loadingElement.append(document.createElement('div'));
            loadingElement.append(document.createElement('div'));

            loadingContainer.append(loadingElement);

            return loadingContainer;
        }
    });

})(Granite.$, Granite);