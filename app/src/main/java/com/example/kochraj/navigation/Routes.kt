package com.example.kochraj.navigation

sealed class Routes(val route: String) {

    data object LoginScreen : Routes("Login")
    data object RegistrationScreen : Routes("Register")
    data object HomeScreen : Routes("Home")
    data object SearchScreen : Routes("Search")
    data object FavoritesScreen : Routes("Favorites")
    data object ProfileScreen : Routes("Profile")
    data object UserDetailsScreen : Routes("UserDetailsScreen")
    data object PersonalDetailsScreen : Routes("PersonalDetailsScreen")
    data object MyAccountScreen : Routes("MyAccountScreen")
    data object ProfilePreviewScreen : Routes("ProfilePreviewScreen")
    data object HelpAndSupportScreen : Routes("HelpAndSupportScreen")
    data object AboutUsScreen : Routes("AboutUsScreen")
}
