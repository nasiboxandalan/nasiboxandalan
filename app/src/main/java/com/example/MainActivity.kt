package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.AgendaViewModel
import com.example.ui.components.AgendaBottomBar
import com.example.ui.components.AgendaNavRail
import com.example.ui.components.AgendaTopBar
import com.example.ui.components.FloatingWhatsAppButton
import com.example.ui.components.MainTab
import com.example.ui.screens.BerandaScreen
import com.example.ui.screens.DetailAcaraScreen
import com.example.ui.screens.KalenderScreen
import com.example.ui.screens.KonsumsiScreen
import com.example.ui.screens.PasangAcaraScreen
import com.example.ui.theme.AgendaKlatenTheme
import com.example.ui.theme.WarmCream
import com.example.util.ExternalIntents
import kotlinx.coroutines.launch

object Routes {
    const val BERANDA = "beranda"
    const val KALENDER = "kalender"
    const val PASANG_ACARA = "pasang-acara"
    const val KONSUMSI = "konsumsi"
    const val DETAIL_ACARA = "acara/{slug}"

    fun detailRoute(slug: String): String = "acara/$slug"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialEventSlug = intent?.getStringExtra("event_slug")
        setContent {
            AgendaKlatenTheme {
                AgendaKlatenApp(initialEventSlug = initialEventSlug)
            }
        }
    }
}

@Composable
fun AgendaKlatenApp(
    initialEventSlug: String? = null,
    viewModel: AgendaViewModel = viewModel()
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val isDetailRoute = currentRoute?.startsWith("acara/") == true

    // Tampilkan FAB WA hanya di Beranda, Detail Acara, dan Konsumsi
    val showWhatsAppFab = currentRoute == Routes.BERANDA ||
        isDetailRoute ||
        currentRoute == Routes.KONSUMSI

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.initializeData(context.applicationContext)
        if (!initialEventSlug.isNullOrBlank()) {
            navController.navigate(Routes.detailRoute(initialEventSlug))
        }
    }

    fun navigateToTab(tab: MainTab) {
        navController.navigate(tab.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun showMessage(message: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavRail = maxWidth >= 600.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = WarmCream,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                AgendaTopBar(
                    showBack = isDetailRoute,
                    onBack = { navController.popBackStack() }
                )
            },
            bottomBar = {
                if (!useNavRail && !isDetailRoute) {
                    AgendaBottomBar(
                        currentRoute = currentRoute,
                        onSelectTab = ::navigateToTab
                    )
                }
            },
            floatingActionButton = {
                if (showWhatsAppFab) {
                    FloatingWhatsAppButton(
                        onClick = {
                            ExternalIntents.openWhatsApp(
                                context,
                                "Halo Nasi Box Andalan (Agenda Klaten), saya ingin pesen konsumsi atau bertanya info acara."
                            )
                        }
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (useNavRail && !isDetailRoute) {
                    AgendaNavRail(
                        currentRoute = currentRoute,
                        onSelectTab = ::navigateToTab
                    )
                }

                NavHost(
                    navController = navController,
                    startDestination = Routes.BERANDA,
                    modifier = Modifier.weight(1f)
                ) {
                    composable(Routes.BERANDA) {
                        BerandaScreen(
                            uiState = uiState,
                            onSearchChange = viewModel::onHomeSearchChange,
                            onQuickFilterSelect = viewModel::onHomeQuickFilterSelect,
                            onWilayahSelect = viewModel::onHomeWilayahSelect,
                            onCategoryToggle = viewModel::onHomeCategoryToggle,
                            onResetFilters = viewModel::resetHomeFilters,
                            onRefreshRemote = { viewModel.refreshRemoteEvents(context.applicationContext) },
                            onEventClick = { slug ->
                                navController.navigate(Routes.detailRoute(slug))
                            },
                            onNavigateToKalender = { navigateToTab(MainTab.KALENDER) },
                            onNavigateToPasangAcara = { navigateToTab(MainTab.PASANG_ACARA) },
                            onNavigateToKonsumsi = { navigateToTab(MainTab.KONSUMSI) }
                        )
                    }

                    composable(Routes.KALENDER) {
                        KalenderScreen(
                            uiState = uiState,
                            onViewModeChange = viewModel::onCalendarViewModeChange,
                            onSearchChange = viewModel::onCalendarSearchChange,
                            onMonthSelect = viewModel::onCalendarMonthSelect,
                            onGridMonthChange = viewModel::onCalendarGridMonthChange,
                            onDaySelect = viewModel::onCalendarDaySelect,
                            onWilayahSelect = viewModel::onCalendarWilayahSelect,
                            onCategorySelect = viewModel::onCalendarCategorySelect,
                            onStatusSelect = viewModel::onCalendarStatusSelect,
                            onResetFilters = viewModel::resetCalendarFilters,
                            onEventClick = { slug ->
                                navController.navigate(Routes.detailRoute(slug))
                            },
                            onNavigateToPasangAcara = { navigateToTab(MainTab.PASANG_ACARA) }
                        )
                    }

                    composable(
                        route = Routes.DETAIL_ACARA,
                        arguments = listOf(navArgument("slug") { type = NavType.StringType })
                    ) { entry ->
                        val slug = entry.arguments?.getString("slug").orEmpty()
                        DetailAcaraScreen(
                            slug = slug,
                            uiState = uiState,
                            onShowMessage = ::showMessage,
                            onEventClick = { nextSlug ->
                                navController.navigate(Routes.detailRoute(nextSlug))
                            },
                            onNavigateToBeranda = { navigateToTab(MainTab.BERANDA) },
                            onNavigateToKalender = { navigateToTab(MainTab.KALENDER) }
                        )
                    }

                    composable(Routes.PASANG_ACARA) {
                        PasangAcaraScreen(
                            onNavigateToBeranda = { navigateToTab(MainTab.BERANDA) }
                        )
                    }

                    composable(Routes.KONSUMSI) {
                        KonsumsiScreen(uiState = uiState)
                    }
                }
            }
        }
    }
}
