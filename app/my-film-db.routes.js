(function(){

    angular
        .module('myFilmDb')
        .config(router);


        function router($routeProvider){
            $routeProvider
                .when('/', {
                    templateUrl: 'pages/add-film/add-film.html',
                    controller: 'AddFilmController',
                    controllerAs: 'addFilmCtrl'
                })
                .otherwise({
                    templateUrl: 'pages/add-film/add-film.html',
                    controller: 'AddFilmController',
                    controllerAs: 'addFilmCtrl'
                });
        };

})();