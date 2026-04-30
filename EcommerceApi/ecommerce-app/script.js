// Utility function to fetch products from API
async function fetchProducts() {
  try {
    const response = await fetch('http://localhost:8080/api/products');

    // Check if response is successful
    if (!response.ok) {
      if (response.status === 404) {
        throw new Error('Products not found (Error 404)');
      } else if (response.status === 500) {
        throw new Error('Server error (Error 500)');
      } else {
        throw new Error(`Unexpected error: ${response.status}`);
      }
    }

    const data = await response.json();
    return data;

  } catch (error) {
    // Log specific error for debugging
    console.error('[fetchProducts Error]', error.message);
    throw error; // Re-throw to handle in render function
  }
}

// Render products to the page
async function renderProducts() {
  const main = document.querySelector('main');
  
  try {
    const products = await fetchProducts();

    if (products.length === 0) {
      // Empty state
      main.innerHTML = <p class="empty-state">No products available at the moment.</p>;
      return;
    }

    // Generate product grid HTML
    const productGrid = `
      <div class="product-grid">
        ${products.map(product => `
          <div class="product-card">
            <img src="${product.image}" alt="${product.name}">
            <h3>${product.name}</h3>
            <p>$${product.price.toFixed(2)}</p>
          </div>
        `).join('')}
      </div>
    `;

    main.innerHTML = productGrid;

  } catch (error) {
    main.innerHTML = <p class="error-message">Failed to load products. Please try again later.</p>;
  }
}

// Call on page load
document.addEventListener('DOMContentLoaded', renderProducts);