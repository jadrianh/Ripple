Add-Type -AssemblyName System.Windows.Forms

# Crear un objeto OpenFileDialog
$fileDialog = New-Object System.Windows.Forms.OpenFileDialog
$fileDialog.Filter = "Image Files (*.png;*.jpg;*.jpeg)|*.png;*.jpg;*.jpeg"
$fileDialog.Title = "Select an image file"

# Mostrar el cuadro de diálogo y verificar si el usuario hizo clic en OK
if ($fileDialog.ShowDialog() -eq 'OK') {
    # La propiedad FileName contiene la ruta completa del archivo seleccionado
    $selectedFile = $fileDialog.FileName
    Write-Output "Selected file: $selectedFile"
} else {
    Write-Output "No file selected"
}