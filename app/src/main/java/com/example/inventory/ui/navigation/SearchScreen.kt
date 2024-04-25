package com.example.inventory.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inventory.R
import com.example.inventory.data.Item
import com.example.inventory.ui.theme.InventoryTheme

object SearchDestination : NavigationDestination {
    override val route = "search"
    override val titleRes = R.string.search_title // Replace R.string.search_title with the appropriate title resource ID
}

@Composable
fun SearchScreen(
    items: List<Item>, // List of all items
    onSearchCompleted: (List<Item>) -> Unit, // Callback for search completion
    onItemClicked: (Item) -> Unit // Callback for item selection
) {
    var searchText by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text(text = stringResource(id = R.string.search_hint)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        val filteredItems = if (searchText.text.isNotBlank()) {
            // Filter items based on search text
            items.filter { item ->
                // Check if item name or ID contains the search text
                item.name.contains(searchText.text, ignoreCase = true) ||
                        item.id.toString().contains(searchText.text, ignoreCase = true)
            }
        } else {
            // If search text is empty, return all items
            items
        }

        // Display search results as clickable items
        filteredItems.forEach { item ->
            Text(
                text = "ID: ${item.id}, Name: ${item.name}",
                modifier = Modifier.clickable { onItemClicked(item) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSearchCompleted(filteredItems) }, // Pass filtered items to callback
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = stringResource(id = R.string.search_button))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen(){
    InventoryTheme {
        SearchScreen(
            listOf(
                Item(1, "Game", 100.0, 20),
                Item(2, "Pen", 200.0, 30),
                Item(3, "TV", 300.0, 50)
            ),
            onSearchCompleted = { searchText ->
                // Mock implementation for onSearchCompleted
                println("Search completed with text: $searchText")
                // You can add more logic here to handle the search results
            },
            onItemClicked = { item ->
                // Mock implementation for onItemClicked
                println("Item clicked: ${item.name}")
                // You can navigate to another screen or perform any other action here
            }
        )
    }
}
