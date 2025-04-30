package com.example.pms.ui.screens.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pms.data.models.Task
import com.example.pms.data.models.TaskPriority
import com.example.pms.data.models.TaskStatus
import java.text.SimpleDateFormat
import java.util.*
import com.example.pms.utils.DateUtils

@Composable
fun TasksScreen(
    viewModel: TasksViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 16.dp)
            ) {
                Text(
                    text = "Tasks",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                SearchFilterBar(
                    searchQuery = state.searchQuery,
                    onSearchQueryChanged = viewModel::onSearchQueryChanged,
                    statusFilter = state.statusFilter,
                    onStatusFilterChanged = viewModel::onStatusFilterChanged,
                    priorityFilter = state.priorityFilter,
                    onPriorityFilterChanged = viewModel::onPriorityFilterChanged,
                    projectFilter = state.projectFilter,
                    projects = state.projects,
                    onProjectFilterChanged = viewModel::onProjectFilterChanged,
                    onClearFilters = viewModel::clearFilters
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAddTaskClicked() },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task"
                )
            }
        }
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.filteredTasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (state.searchQuery.isNotEmpty() || state.statusFilter != null || 
                                  state.priorityFilter != null || state.projectFilter != null) {
                            "No tasks match your filters"
                        } else {
                            "No tasks yet"
                        },
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = if (state.searchQuery.isNotEmpty() || state.statusFilter != null || 
                                  state.priorityFilter != null || state.projectFilter != null) {
                            "Try adjusting your search or filters"
                        } else {
                            "Create your first task to get started"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    if (state.searchQuery.isEmpty() && state.statusFilter == null && 
                        state.priorityFilter == null && state.projectFilter == null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { viewModel.onAddTaskClicked() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = "Add Task")
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 220.dp)
            ) {
                items(state.filteredTasks) { task ->
                    TaskListItem(
                        task = task,
                        projectName = state.projects.find { it.id == task.projectId }?.name ?: "Unknown Project",
                        onStatusChange = { newStatus ->
                            viewModel.updateTaskStatus(task, newStatus)
                        },
                        onDeleteClick = { viewModel.deleteTask(task.id) }
                    )
                }
            }
        }
        
        if (state.showAddDialog) {
            AddTaskDialog(
                task = state.newTask,
                projects = state.projects,
                onTitleChanged = viewModel::onNewTaskTitleChanged,
                onDescriptionChanged = viewModel::onNewTaskDescriptionChanged,
                onProjectChanged = viewModel::onNewTaskProjectChanged,
                onStatusChanged = viewModel::onNewTaskStatusChanged,
                onPriorityChanged = viewModel::onNewTaskPriorityChanged,
                onDueDateChanged = viewModel::onNewTaskDueDateChanged,
                onDismiss = viewModel::onDismissAddDialog,
                onConfirm = viewModel::onCreateTask
            )
        }
    }
}

@Composable
fun SearchFilterBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    statusFilter: TaskStatus?,
    onStatusFilterChanged: (TaskStatus?) -> Unit,
    priorityFilter: TaskPriority?,
    onPriorityFilterChanged: (TaskPriority?) -> Unit,
    projectFilter: String?,
    projects: List<com.example.pms.data.models.Project>,
    onProjectFilterChanged: (String?) -> Unit,
    onClearFilters: () -> Unit
) {
    var showStatusMenu by remember { mutableStateOf(false) }
    var showPriorityMenu by remember { mutableStateOf(false) }
    var showProjectMenu by remember { mutableStateOf(false) }
    
    Column {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search tasks...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChanged("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear search")
                    }
                }
            },
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Status Filter
            Box {
                FilterChip(
                    selected = statusFilter != null,
                    onClick = { showStatusMenu = true },
                    label = { 
                        Text(
                            text = statusFilter?.name?.replace('_', ' ') ?: "Status",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        ) 
                    },
                    leadingIcon = { 
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
                
                DropdownMenu(
                    expanded = showStatusMenu,
                    onDismissRequest = { showStatusMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("All") },
                        onClick = {
                            onStatusFilterChanged(null)
                            showStatusMenu = false
                        }
                    )
                    
                    TaskStatus.values().forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status.name.replace('_', ' ')) },
                            onClick = {
                                onStatusFilterChanged(status)
                                showStatusMenu = false
                            }
                        )
                    }
                }
            }
            
            // Priority Filter
            Box {
                FilterChip(
                    selected = priorityFilter != null,
                    onClick = { showPriorityMenu = true },
                    label = { 
                        Text(
                            text = priorityFilter?.name ?: "Priority",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        ) 
                    },
                    leadingIcon = { 
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
                
                DropdownMenu(
                    expanded = showPriorityMenu,
                    onDismissRequest = { showPriorityMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("All") },
                        onClick = {
                            onPriorityFilterChanged(null)
                            showPriorityMenu = false
                        }
                    )
                    
                    TaskPriority.values().forEach { priority ->
                        DropdownMenuItem(
                            text = { Text(priority.name) },
                            onClick = {
                                onPriorityFilterChanged(priority)
                                showPriorityMenu = false
                            }
                        )
                    }
                }
            }
            
            // Project Filter
            Box {
                FilterChip(
                    selected = projectFilter != null,
                    onClick = { showProjectMenu = true },
                    label = { 
                        Text(
                            text = projects.find { it.id == projectFilter }?.name ?: "Project",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        ) 
                    },
                    leadingIcon = { 
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
                
                DropdownMenu(
                    expanded = showProjectMenu,
                    onDismissRequest = { showProjectMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("All Projects") },
                        onClick = {
                            onProjectFilterChanged(null)
                            showProjectMenu = false
                        }
                    )
                    
                    projects.forEach { project ->
                        DropdownMenuItem(
                            text = { Text(project.name) },
                            onClick = {
                                onProjectFilterChanged(project.id)
                                showProjectMenu = false
                            }
                        )
                    }
                }
            }
            
            // Clear All Filters
            if (searchQuery.isNotEmpty() || statusFilter != null || 
                priorityFilter != null || projectFilter != null) {
                FilterChip(
                    selected = false,
                    onClick = onClearFilters,
                    label = { Text("Clear") },
                    leadingIcon = { 
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun TaskListItem(
    task: Task,
    projectName: String,
    onStatusChange: (TaskStatus) -> Unit,
    onDeleteClick: () -> Unit
) {
    var showStatusMenu by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = projectName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            if (task.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Priority indicator
                val priorityColor = when (task.priority) {
                    TaskPriority.HIGH -> MaterialTheme.colorScheme.error
                    TaskPriority.MEDIUM -> MaterialTheme.colorScheme.tertiary
                    TaskPriority.LOW -> MaterialTheme.colorScheme.primary
                }
                
                Surface(
                    color = priorityColor.copy(alpha = 0.1f),
                    contentColor = priorityColor,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = task.priority.name,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Due date
                task.dueDate?.let {
                    val isOverdue = it.before(Date()) && task.status != TaskStatus.COMPLETED
                    val textColor = if (isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    
                    Text(
                        text = "Due: ${DateUtils.formatDate(it)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Status with dropdown
                Box {
                    Button(
                        onClick = { showStatusMenu = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (task.status) {
                                TaskStatus.COMPLETED -> MaterialTheme.colorScheme.primary
                                TaskStatus.IN_PROGRESS -> MaterialTheme.colorScheme.secondary
                                TaskStatus.TODO -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                    ) {
                        Text(task.status.name.replace('_', ' '))
                    }
                    
                    DropdownMenu(
                        expanded = showStatusMenu,
                        onDismissRequest = { showStatusMenu = false }
                    ) {
                        TaskStatus.values().forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.name.replace('_', ' ')) },
                                onClick = {
                                    onStatusChange(status)
                                    showStatusMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    task: Task,
    projects: List<com.example.pms.data.models.Project>,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onProjectChanged: (String) -> Unit,
    onStatusChanged: (TaskStatus) -> Unit,
    onPriorityChanged: (TaskPriority) -> Unit,
    onDueDateChanged: (Date?) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showStatusDropdown by remember { mutableStateOf(false) }
    var showPriorityDropdown by remember { mutableStateOf(false) }
    var showProjectDropdown by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Task") },
        text = {
            Column {
                TextField(
                    value = task.title,
                    onValueChange = onTitleChanged,
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                TextField(
                    value = task.description,
                    onValueChange = onDescriptionChanged,
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Project dropdown
                Box {
                    TextField(
                        value = projects.find { it.id == task.projectId }?.name ?: "Select Project",
                        onValueChange = {},
                        label = { Text("Project") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showProjectDropdown = true }) {
                                Icon(Icons.Default.ArrowDropDown, "Select Project")
                            }
                        }
                    )
                    
                    DropdownMenu(
                        expanded = showProjectDropdown,
                        onDismissRequest = { showProjectDropdown = false }
                    ) {
                        projects.forEach { project ->
                            DropdownMenuItem(
                                text = { Text(project.name) },
                                onClick = {
                                    onProjectChanged(project.id)
                                    showProjectDropdown = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Status dropdown
                Box {
                    TextField(
                        value = task.status.name.replace('_', ' '),
                        onValueChange = {},
                        label = { Text("Status") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showStatusDropdown = true }) {
                                Icon(Icons.Default.ArrowDropDown, "Select Status")
                            }
                        }
                    )
                    
                    DropdownMenu(
                        expanded = showStatusDropdown,
                        onDismissRequest = { showStatusDropdown = false }
                    ) {
                        TaskStatus.values().forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.name.replace('_', ' ')) },
                                onClick = {
                                    onStatusChanged(status)
                                    showStatusDropdown = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Priority dropdown
                Box {
                    TextField(
                        value = task.priority.name,
                        onValueChange = {},
                        label = { Text("Priority") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showPriorityDropdown = true }) {
                                Icon(Icons.Default.ArrowDropDown, "Select Priority")
                            }
                        }
                    )
                    
                    DropdownMenu(
                        expanded = showPriorityDropdown,
                        onDismissRequest = { showPriorityDropdown = false }
                    ) {
                        TaskPriority.values().forEach { priority ->
                            DropdownMenuItem(
                                text = { Text(priority.name) },
                                onClick = {
                                    onPriorityChanged(priority)
                                    showPriorityDropdown = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Due date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Due Date: ${DateUtils.formatDate(task.dueDate)}",
                        modifier = Modifier.weight(1f)
                    )
                    
                    TextButton(onClick = { showDatePicker = true }) {
                        Text("Change")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = task.title.isNotBlank() && task.projectId.isNotBlank()
            ) {
                Text("Add Task")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
    
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = task.dueDate?.time
        )
        
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val date = datePickerState.selectedDateMillis?.let { Date(it) }
                        onDueDateChanged(date)
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// Using DateUtils for date formatting
