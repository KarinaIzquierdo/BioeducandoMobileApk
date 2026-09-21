package com.bioeducando.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bioeducando.mobile.ui.screens.admin.AdminComunidadScreen
import com.bioeducando.mobile.ui.screens.admin.AdminDashboardScreen
import com.bioeducando.mobile.ui.screens.admin.AdminDrawer
import com.bioeducando.mobile.ui.screens.admin.AdminEditarRetoScreen
import com.bioeducando.mobile.ui.screens.admin.AdminCrearProyectoScreen
import com.bioeducando.mobile.ui.screens.admin.AdminLoginScreen
import com.bioeducando.mobile.ui.screens.admin.AdminPraeScreen
import com.bioeducando.mobile.ui.screens.admin.AdminRetosScreen
import com.bioeducando.mobile.ui.screens.admin.AdminSteamScreen
import com.bioeducando.mobile.ui.screens.admin.AdminWelcomeScreen
import com.bioeducando.mobile.ui.screens.usuario.UsuarioComunidadScreen
import com.bioeducando.mobile.ui.screens.usuario.UsuarioEcoEstudioScreen
import com.bioeducando.mobile.ui.screens.usuario.UsuarioNoticiasScreen
import com.bioeducando.mobile.ui.screens.usuario.UsuarioPerfilScreen
import com.bioeducando.mobile.ui.screens.usuario.UsuarioPraeScreen
import com.bioeducando.mobile.ui.screens.usuario.UsuarioRetosScreen
import com.bioeducando.mobile.ui.screens.usuario.UsuarioSteamScreen
import com.bioeducando.mobile.data.local.TokenManager
import com.bioeducando.mobile.ui.screens.admin.AdminNoticiasScreen
import com.bioeducando.mobile.ui.screens.admin.AdminPerfilScreen
import com.bioeducando.mobile.ui.screens.admin.AdminUsuariosScreen
import com.bioeducando.mobile.ui.screens.publico.PublicComunidadScreen
import com.bioeducando.mobile.ui.screens.publico.PublicNoticiasScreen
import com.bioeducando.mobile.ui.screens.publico.PublicPlaceholderScreen
import com.bioeducando.mobile.ui.screens.publico.PublicRetosScreen
import com.bioeducando.mobile.ui.theme.BioeducandoMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioeducandoMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val tokenManager = remember { TokenManager(this@MainActivity) }
                    var adminDrawerOpen by remember { mutableStateOf(false) }

                    Box(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = "admin_welcome"
                    ) {
                        composable("admin_welcome") {
                            AdminWelcomeScreen(
                                onStartClick = {
                                    navController.navigate("admin_login")
                                },
                                onComunidadClick = {
                                    navController.navigate("public_comunidad")
                                },
                                onNoticiasClick = {
                                    navController.navigate("public_noticias")
                                },
                                onRetosClick = {
                                    navController.navigate("public_retos")
                                }
                            )
                        }

                        composable("public_comunidad") {
                            PublicComunidadScreen(
                                onBackClick = { navController.popBackStack() },
                                onLoginClick = { navController.navigate("admin_login") }
                            )
                        }

                        composable("public_noticias") {
                            PublicNoticiasScreen(
                                onBackClick = { navController.popBackStack() },
                                onLoginClick = { navController.navigate("admin_login") }
                            )
                        }

                        composable("public_retos") {
                            PublicRetosScreen(
                                onBackClick = { navController.popBackStack() },
                                onLoginClick = { navController.navigate("admin_login") }
                            )
                        }

                        composable("admin_login") {
                            AdminLoginScreen(
                                onAuthSuccess = {
                                    val role = tokenManager.getRole()
                                    if (role == "1") {
                                        navController.navigate("admin_dashboard") {
                                            popUpTo("admin_login") { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate("usuario_perfil") {
                                            popUpTo("admin_login") { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }

                        composable("admin_dashboard") {
                            AdminDashboardScreen(
                                onMenuClick = { adminDrawerOpen = true }
                            )
                        }

                        composable("admin_noticias") {
                            AdminNoticiasScreen(
                                onMenuClick = { adminDrawerOpen = true }
                            )
                        }

                        composable("admin_retos") {
                            AdminRetosScreen(
                                onMenuClick = { adminDrawerOpen = true },
                                onEditClick = { retoId ->
                                    navController.navigate("admin_editar_reto/$retoId")
                                }
                            )
                        }

                        composable("admin_editar_reto/{retoId}") { backStackEntry ->
                            val retoId = backStackEntry.arguments?.getString("retoId")?.toIntOrNull() ?: -1
                            AdminEditarRetoScreen(
                                retoId = retoId,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable("admin_comunidad") {
                            AdminComunidadScreen(
                                onMenuClick = { adminDrawerOpen = true }
                            )
                        }

                        composable("admin_steam") {
                            AdminSteamScreen(
                                onMenuClick = { adminDrawerOpen = true },
                                onNuevoProyectoClick = { navController.navigate("admin_nuevo_proyecto_steam") }
                            )
                        }

                        composable("admin_nuevo_proyecto_steam") {
                            AdminCrearProyectoScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable("admin_prae") {
                            AdminPraeScreen(
                                onMenuClick = { adminDrawerOpen = true }
                            )
                        }

                        composable("admin_perfil") {
                            AdminPerfilScreen(
                                onMenuClick = { adminDrawerOpen = true }
                            )
                        }

                        composable("admin_usuarios") {
                            AdminUsuariosScreen(
                                onMenuClick = { adminDrawerOpen = true }
                            )
                        }

                        composable("usuario_perfil") {
                            UsuarioPerfilScreen(
                                onMenuClick = { },
                                onLogoutClick = {
                                    navController.navigate("admin_login") {
                                        popUpTo("usuario_perfil") { inclusive = true }
                                    }
                                },
                                onRetosClick = {
                                    navController.navigate("usuario_retos") {
                                        popUpTo("usuario_perfil") { inclusive = false }
                                    }
                                },
                                onComunidadClick = {
                                    navController.navigate("usuario_comunidad")
                                },
                                onSteamClick = {
                                    navController.navigate("usuario_steam")
                                },
                                onPraeClick = {
                                    navController.navigate("usuario_prae")
                                },
                                onNoticiasClick = {
                                    navController.navigate("usuario_noticias")
                                },
                                onEcoEstudioClick = {
                                    navController.navigate("usuario_eco_estudio")
                                }
                            )
                        }

                        composable("usuario_retos") {
                            UsuarioRetosScreen(
                                onMenuClick = { },
                                onLogoutClick = {
                                    navController.navigate("admin_login") {
                                        popUpTo("usuario_perfil") { inclusive = true }
                                    }
                                },
                                onPerfilClick = {
                                    navController.popBackStack()
                                },
                                onComunidadClick = {
                                    navController.navigate("usuario_comunidad")
                                },
                                onSteamClick = {
                                    navController.navigate("usuario_steam")
                                },
                                onPraeClick = {
                                    navController.navigate("usuario_prae")
                                },
                                onNoticiasClick = {
                                    navController.navigate("usuario_noticias")
                                },
                                onEcoEstudioClick = {
                                    navController.navigate("usuario_eco_estudio")
                                }
                            )
                        }

                        composable("usuario_comunidad") {
                            UsuarioComunidadScreen(
                                onLogoutClick = {
                                    navController.navigate("admin_login") {
                                        popUpTo("usuario_perfil") { inclusive = true }
                                    }
                                },
                                onRetosClick = {
                                    navController.navigate("usuario_retos") {
                                        popUpTo("usuario_comunidad") { inclusive = false }
                                    }
                                },
                                onPerfilClick = {
                                    navController.popBackStack()
                                },
                                onSteamClick = {
                                    navController.navigate("usuario_steam")
                                },
                                onPraeClick = {
                                    navController.navigate("usuario_prae")
                                },
                                onNoticiasClick = {
                                    navController.navigate("usuario_noticias")
                                },
                                onEcoEstudioClick = {
                                    navController.navigate("usuario_eco_estudio")
                                }
                            )
                        }

                        composable("usuario_steam") {
                            UsuarioSteamScreen(
                                onLogoutClick = {
                                    navController.navigate("admin_login") {
                                        popUpTo("usuario_perfil") { inclusive = true }
                                    }
                                },
                                onRetosClick = {
                                    navController.navigate("usuario_retos") {
                                        popUpTo("usuario_steam") { inclusive = false }
                                    }
                                },
                                onPerfilClick = {
                                    navController.popBackStack()
                                },
                                onComunidadClick = {
                                    navController.navigate("usuario_comunidad")
                                },
                                onPraeClick = {
                                    navController.navigate("usuario_prae")
                                },
                                onNoticiasClick = {
                                    navController.navigate("usuario_noticias")
                                },
                                onEcoEstudioClick = {
                                    navController.navigate("usuario_eco_estudio")
                                }
                            )
                        }

                        composable("usuario_prae") {
                            UsuarioPraeScreen(
                                onLogoutClick = {
                                    navController.navigate("admin_login") {
                                        popUpTo("usuario_perfil") { inclusive = true }
                                    }
                                },
                                onRetosClick = {
                                    navController.navigate("usuario_retos") {
                                        popUpTo("usuario_prae") { inclusive = false }
                                    }
                                },
                                onComunidadClick = {
                                    navController.navigate("usuario_comunidad")
                                },
                                onSteamClick = {
                                    navController.navigate("usuario_steam")
                                },
                                onPerfilClick = {
                                    navController.popBackStack()
                                },
                                onNoticiasClick = {
                                    navController.navigate("usuario_noticias")
                                }
                            )
                        }

                        composable("usuario_noticias") {
                            UsuarioNoticiasScreen(
                                onLogoutClick = {
                                    navController.navigate("admin_login") {
                                        popUpTo("usuario_perfil") { inclusive = true }
                                    }
                                },
                                onRetosClick = {
                                    navController.navigate("usuario_retos") {
                                        popUpTo("usuario_noticias") { inclusive = false }
                                    }
                                },
                                onComunidadClick = {
                                    navController.navigate("usuario_comunidad")
                                },
                                onSteamClick = {
                                    navController.navigate("usuario_steam")
                                },
                                onPraeClick = {
                                    navController.navigate("usuario_prae")
                                },
                                onPerfilClick = {
                                    navController.popBackStack()
                                },
                                onEcoEstudioClick = {
                                    navController.navigate("usuario_eco_estudio")
                                }
                            )
                        }

                        composable("usuario_eco_estudio") {
                            UsuarioEcoEstudioScreen(
                                onLogoutClick = {
                                    navController.navigate("admin_login") {
                                        popUpTo("usuario_perfil") { inclusive = true }
                                    }
                                },
                                onRetosClick = {
                                    navController.navigate("usuario_retos") {
                                        popUpTo("usuario_eco_estudio") { inclusive = false }
                                    }
                                },
                                onComunidadClick = {
                                    navController.navigate("usuario_comunidad")
                                },
                                onSteamClick = {
                                    navController.navigate("usuario_steam")
                                },
                                onPraeClick = {
                                    navController.navigate("usuario_prae")
                                },
                                onNoticiasClick = {
                                    navController.navigate("usuario_noticias")
                                },
                                onPerfilClick = {
                                    navController.popBackStack()
                                }
                            )
                        }

                    }

                    AdminDrawer(
                        isOpen = adminDrawerOpen,
                        onClose = { adminDrawerOpen = false },
                        onLogout = {
                            navController.navigate("admin_login") {
                                popUpTo("admin_dashboard") { inclusive = true }
                            }
                        },
                        onComunidadClick = {
                            navController.navigate("admin_comunidad")
                        },
                        onRetosClick = {
                            navController.navigate("admin_retos")
                        },
                        onSteamClick = {
                            navController.navigate("admin_steam")
                        },
                        onPraeClick = {
                            navController.navigate("admin_prae")
                        },
                        onNoticiasClick = {
                            navController.navigate("admin_noticias")
                        },
                        onPerfilClick = {
                            navController.navigate("admin_perfil")
                        },
                        onUsuariosClick = {
                            navController.navigate("admin_usuarios")
                        }
                    )
                    }
                }
            }
        }
    }
}