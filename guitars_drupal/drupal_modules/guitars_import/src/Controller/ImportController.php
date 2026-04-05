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
    $errors = [];
    
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

        // Map type field if provided
        if (!empty($item['type'])) {
          $node_data['field_type'] = $item['type'];
        }

        // Handle image if provided
        if (!empty($item['img'])) {
          $image_path = $item['img'];
          
          // Get the Drupal public files directory
          $public_path = \Drupal::service('file_system')->realpath('public://');
          
          // Try multiple possible source paths
          $drupal_root = \Drupal::getContainer()->getParameter('app.root');
          $source_paths = [
            $drupal_root . '/' . $image_path,
            '/var/www/html/' . $image_path,
            '/var/www/html/themes/custom/guitars_theme/' . basename($image_path),
          ];
          
          $source_file_path = NULL;
          foreach ($source_paths as $path) {
            if (file_exists($path)) {
              $source_file_path = $path;
              break;
            }
          }
          
          if ($source_file_path) {
            // Copy image to Drupal public files directory
            $filename = basename($image_path);
            $destination = $public_path . '/guitars/' . $filename;
            
            // Create directory if it doesn't exist
            if (!is_dir($public_path . '/guitars')) {
              mkdir($public_path . '/guitars', 0755, TRUE);
            }
            
            // Copy the file
            if (copy($source_file_path, $destination)) {
              // Create a File entity
              $file = File::create([
                'uri' => 'public://guitars/' . $filename,
                'filename' => $filename,
                'status' => 1, // FILE_STATUS_PERMANENT
              ]);
              $file->save();
              
              // Attach to the node
              $node_data['field_image'] = [
                'target_id' => $file->id(),
                'alt' => $item['title'],
                'title' => $item['title'],
              ];
            } else {
              $errors[] = $this->t('Failed to copy image for @title', ['@title' => $item['title']]);
            }
          } else {
            $errors[] = $this->t('Image file not found for @title (tried paths from JSON)', ['@title' => $item['title']]);
          }
        }

        $node = Node::create($node_data);
        $node->save();
        $count++;
      }
    }

    $message = $this->t('Successfully imported @count guitars into the database.', ['@count' => $count]);
    if (!empty($errors)) {
      $message .= '<br/><strong>Warnings:</strong><br/>' . implode('<br/>', $errors);
    }
    
    return [
      '#markup' => $message,
    ];
  }

}
