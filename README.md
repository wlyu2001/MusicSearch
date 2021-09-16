# Architecture

* Fragment - ViewModel - Repository - Retrofit - Room(local data source, not implemented)

* Dependency Injection (Dagger-Hilt)

* Uri based navigation, support deep linking


# Asynchronous Operation

* Coroutine


# Test

* Local Unit Test: SearchArtistViewModelTest, test spinner and error message, using Repository fake double.

* Instrumented Large Test: MainActivityTest, test the complete flow, using Espresso with injected mock ContentAPI. IdlingResource is used to validate asynchronous operations.