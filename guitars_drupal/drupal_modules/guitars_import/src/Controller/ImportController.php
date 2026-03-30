<?php

namespace Drupal\guitars_import\Controller;

use Drupal\Core\Controller\ControllerBase;
use Drupal\node\Entity\Node;
use Drupal\file\Entity\File;

/**
 * Controller for importing Guitars.
 */
class ImportController extends ControllerBase {

  /**
   * Reads JSON data and creates Guitar nodes.
   */
  public function import() {
    $module_handler = \Drupal::service('extension.list.module');
    $module_path = $module_handler->getPath('guitars_import');
    $json_path = $module_path . '/data/guitars.json';

    if (!file_exists($json_path)) {
      return [
        '#markup' => $this->t('Data file not found at @path. Please create it with your guitar array.', ['@path' => $json_path]),
      ];
    }

    $json_data = file_get_contents($json_path);
    $guitars = json_decode($json_data, TRUE);

    if (json_last_error() !== JSON_ERROR_NONE) {
      return [
        '#markup' => $this->t('Invalid JSON format in the data file.'),
      ];
    }

    $count = 0;
    foreach ($guitars as $item) {
      // Check if guitar already exists to avoid duplicates
      $query = \Drupal::entityQuery('node')
        ->condition('type', 'guitar')
        ->condition('title', $item['title'])
        ->accessCheck(FALSE);
      
      $nids = $query->execute();
      
      if (empty($nids)) {
        // Create the new node
        $node_data = [
          'type' => 'guitar',
          'title' => $item['title'],
          'body' => [
            'value' => $item['description'] ?? '',
            'format' => 'full_html',
          ],
        ];

        // Map taxonomy/type field if provided
        if (!empty($item['type'])) {
          // Assuming a simple text list for now. If taxonomy, you'd need term IDs.
          $node_data['field_type'] = $item['type'];
        }

        $node = Node::create($node_data);
        $node->save();
        $count++;
      }
    }

    return [
      '#markup' => $this->t('Successfully imported @count guitars into the database.', ['@count' => $count]),
    ];
  }

}
